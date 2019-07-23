package pt.iscte.paddle.roles;

public interface IVariableRole {
	String getName();
	
	IVariableRole NONE = new IVariableRole() {
		public String getName() {
			return "no role";
		}
		
		@Override
		public String toString() {
			return getName();
		}
	};
}
