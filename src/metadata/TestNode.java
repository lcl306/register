package metadata;

public class TestNode {
	

	@RexassNote(assignTo="kim", severity=0)
	public String getElement(){
		return "1";
	}
	
	@RexassNote(assignTo="kim2")
	public String getElement2(){
		return "2";
	}
	
	@RexassNote()
	public String getElement3(){
		return "3";
	}

}
