package pt.iscte.paddle.javasde;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class JavaSDElipsePart extends EditorPart {

	private String name;
	private ClassWidget classWidget;

	@Override
	public void doSave(IProgressMonitor monitor) {
		StringBuffer buffer = new StringBuffer();
		classWidget.toCode(buffer);
		System.out.println(buffer);
	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		name = input.getName();
		setPartName(name);
		name = name.substring(0, name.indexOf('.'));
	}

	@Override
	public boolean isDirty() {
		return true;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);

		Composite child = new Composite(sc, SWT.NONE);
		child.setLayout(new FillLayout());
		classWidget = new ClassWidget(child, name, new UiMode());

		sc.setContent(child);
	    sc.setMinSize(400, 400);
	    sc.setExpandHorizontal(true);
	    sc.setExpandVertical(true);
	    
	    Menu menu = new Menu(classWidget);
	    MenuItem item = new MenuItem(menu, SWT.TOGGLE);
	    item.setText("test");
	    item.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent e) {
				classWidget.hideAddLabels();
			}
		});
	    classWidget.setMenu(menu);
	    
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}