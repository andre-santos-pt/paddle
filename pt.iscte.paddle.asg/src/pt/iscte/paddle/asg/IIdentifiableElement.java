package pt.iscte.paddle.asg;

public interface IIdentifiableElement extends IProgramElement {
	String getId(); // not null, not empty
	
	static boolean isValidIdentifier(String id) {
		return id != null && id.matches("[a-zA-Z][a-zA-Z0-9]*");
	}
}
