package pt.iscte.paddle.asg;

public interface ISelection extends IControlStructure {
//	IBlock getSelectionBlock(); // not null
//	IBlock addAlternativeBlock(); // create

	IBlock getAlternativeBlock(); // may be null

	default boolean hasAlternativeBlock() {
		return getAlternativeBlock() != null;
	}
}