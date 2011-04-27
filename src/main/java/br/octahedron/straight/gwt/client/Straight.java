package br.octahedron.straight.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Straight implements EntryPoint {
	
	public void onModuleLoad() {
		final BankFacadeAsync bankFacade = (BankFacadeAsync) GWT.create(BankFacade.class);
		
		RootPanel rootPanel = RootPanel.get();
		rootPanel.add(new ViewManager(bankFacade));
	}
}
