package minas.mina.filter.mytextline;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class MyTextLineCodecDecoder implements ProtocolDecoder {
	
	private Charset charset;  // 编码格式
	
	private String delimiter;  // 文件分隔符
	
	private IoBuffer delimBuf;  // 文件分隔符匹配变量
	
	private static String CONTEXT = MyTextLineCodecDecoder.class.getName()+".context";
	
	public MyTextLineCodecDecoder(Charset charset, String delimiter){
		this.charset = charset;
		this.delimiter = delimiter;
	}

	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)throws Exception {
		Context ctx = getContext(session);
		delimiter = delimiter==null||"".equals(delimiter.trim())?"\r\n":delimiter;
		charset = charset==null?Charset.forName("utf-8"):charset;
		decodeAuto(ctx, in, out);
	}
	
	private Context getContext(IoSession session){
		Context ctx = (Context)session.getAttribute(CONTEXT);
		if(ctx == null){
			ctx = new Context();
			session.setAttribute(CONTEXT, ctx);
		}
		return ctx;
	}
	
	private void decodeAuto(Context ctx, IoBuffer in, ProtocolDecoderOutput out) throws CharacterCodingException{
		//boolean mark = false;
		int matchCount = ctx.getMatchCount();  //从未完成任务中，取出已经匹配的文本换行符的个数
		if(delimBuf==null){
			IoBuffer tmp = IoBuffer.allocate(2).setAutoExpand(true);
			tmp.putString(delimiter, charset.newEncoder());
			tmp.flip();
			delimBuf = tmp;
		}
		int oldPos = in.position();
		int oldLimit = in.limit();
		while(in.hasRemaining()){ // inRemaining=limit-position, hasRemaining=position<limit,在flip()使用之后用
			byte b = in.get();
			if(delimBuf.get(matchCount)==b){
				matchCount++;
				if(matchCount == delimBuf.limit()){ //当前匹配到字节个数与文本换行符字节个数相同，匹配结束
					int pos = in.position(); //获得当前匹配到的position
					in.limit(pos);
					in.position(oldPos);
					ctx.append(in);
					in.limit(oldLimit);
					in.position(pos);
					IoBuffer buf = ctx.getBuf();
					buf.flip();
					buf.limit(buf.limit()-matchCount); //去掉匹配数据中的文本换行符
					try{
						out.write(buf.getString(charset.newDecoder()));  
					}finally{
						buf.clear();  
					}
					oldPos = pos;
					matchCount = 0;
				}
			}else{
				//如果matchCount==0，则继续匹配 
				//如果matchCount>0，说明没有匹配到文本换行符的中的前一个匹配成功字节的下一个字节，跳转到匹配失败字符处，并置matchCount=0，继续匹配
				in.position(in.position()-matchCount);
				matchCount = 0;
			}
			/*switch(b){
			case '\r': break;
			case '\n': mark = true; break;
			default: ctx.getBuf().put(b);  //读入ByteBuffer
			}
			if(mark){
				IoBuffer t_buf = ctx.getBuf();
				t_buf.flip();  // limit=position, position=0，标明实际空间，为写出做准备
				try{
					out.write(t_buf.getString(charset.newDecoder()));  //从ByteBuffer中写出
				}finally{
					t_buf.clear();  // limit=capacity position=0
				}
			}*/
		}
		in.position(oldPos);
		ctx.append(in);
		ctx.setMatchCount(matchCount);
	}
	
	private class Context{
		private IoBuffer buf;
		private CharsetDecoder decoder;
		private int matchCount = 0;  //换行符个数
		public Context(){
			// capacity=100,且可扩展
			buf = IoBuffer.allocate(100).setAutoExpand(true);
		}
		//重置
		public void reset(){
			matchCount = 0;
			decoder.reset();
		}
		//追加数据
		public void append(IoBuffer in){
			getBuf().put(in);
		}
		public IoBuffer getBuf(){
			return buf;
		}
		public int getMatchCount() {
			return matchCount;
		}
		public void setMatchCount(int matchCount) {
			this.matchCount = matchCount;
		}
	}

	public void dispose(IoSession session) throws Exception {
		/*Context ctx = (Context)session.getAttribute(CONTEXT);
		if(ctx!=null){
			session.removeAttribute(CONTEXT);
		}*/
	}

	public void finishDecode(IoSession session, ProtocolDecoderOutput out)throws Exception {

	}

}
