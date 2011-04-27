/*
 *  Straight - A system to manage financial demands for small and decentralized
 *  organizations.
 *  Copyright (C) 2011  Octahedron 
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.octahedron.straight.gwt.client;

import br.octahedron.straight.gwt.client.panel.BankPanel;
import br.octahedron.straight.gwt.client.panel.PanelType;
import br.octahedron.straight.gwt.client.panel.ViewPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author vitoravelino
 *
 */
public class ViewManager extends Composite implements ValueChangeHandler<String> {

	private static ViewManagerUiBinder uiBinder = GWT.create(ViewManagerUiBinder.class);

	interface ViewManagerUiBinder extends UiBinder<Widget, ViewManager> { }
	
	@UiField DockLayoutPanel viewManager;
	
	private Widget currentPanel;

	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	public ViewManager() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public ViewManager(BankFacadeAsync facade) {
		initWidget(uiBinder.createAndBindUi(this));

		viewManager.setHeight(Window.getClientHeight() - 10 + "px");
		
		History.addValueChangeHandler(this);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)
	 */
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		this.show(this.getPanel(event.getValue()));
	}
	
	private Widget getPanel(String panel) {
		PanelType type = PanelType.getPanelType(panel);
		switch (type) {
			case BANK:
				return new BankPanel();
	
			default:
				break;
		}
		return null;
	}
	
	private void show(Widget panel) {
		if (currentPanel != null) {
			viewManager.remove(currentPanel);
		}
		viewManager.addWest(panel, 33);
		currentPanel = panel;
	}

}
