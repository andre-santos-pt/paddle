package pt.iscte.paddle.javali2asg;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import pt.iscte.paddle.asg.semantics.ISemanticProblem;

public interface ISourceLocation {
	int getStartChar();
	
	int getEndChar();
	
	default IMarker createMarker(IResource r, ISemanticProblem p) {
		try {
			IMarker marker = r.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.MESSAGE, p.getMessage());
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
			//                 marker.setAttribute(IMarker.LINE_NUMBER, line);
			marker.setAttribute(IMarker.CHAR_START, getStartChar());
			marker.setAttribute(IMarker.CHAR_END, getEndChar());
			return marker;
		} catch (CoreException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}