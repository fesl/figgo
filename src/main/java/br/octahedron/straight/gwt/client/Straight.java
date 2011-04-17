package br.octahedron.straight.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalSplitPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.logical.shared.AttachEvent;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Straight implements EntryPoint {
	
	public void onModuleLoad() {
		final BankFacadeAsync facade = (BankFacadeAsync) GWT.create(BankFacade.class);
		
		RootPanel rootPanel = RootPanel.get();
		rootPanel.setSize("960px", "100%");
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		rootPanel.add(verticalPanel, 10, 10);
		verticalPanel.setSize("400px", "32px");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);
		horizontalPanel.setSize("100%", "66");
		
		Label label = new Label("Perfil");
		label.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				facade.myMethod("lalala", new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						Window.alert("lalala");
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("lalala");
						
					}
				});
			}
		});
		horizontalPanel.add(label);
		label.setSize("100px", "15px");
		
		Hyperlink hprlnkServios = new Hyperlink("Serviços", false, "newHistoryToken");
		horizontalPanel.add(hprlnkServios);
		hprlnkServios.setSize("100px", "15px");
		
		Hyperlink hprlnkFinanas = new Hyperlink("Finanças", false, "newHistoryToken");
		horizontalPanel.add(hprlnkFinanas);
		hprlnkFinanas.setSize("100px", "15px");
		
		Hyperlink hprlnkBanco = new Hyperlink("Banco", false, "newHistoryToken");
		horizontalPanel.add(hprlnkBanco);
		hprlnkBanco.setSize("100px", "15px");
		
		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		rootPanel.add(horizontalPanel_2, 10, 48);
		horizontalPanel_2.setSize("400px", "298px");
		
		VerticalPanel verticalPanel_1 = new VerticalPanel();
		horizontalPanel_2.add(verticalPanel_1);
		verticalPanel_1.setSize("283px", "296px");
		
		VerticalPanel horizontalPanel_1 = new VerticalPanel();
		horizontalPanel_2.add(horizontalPanel_1);
		horizontalPanel_1.setSize("112px", "65px");
		
		Hyperlink hprlnkExtrato = new Hyperlink("Extrato", false, "newHistoryToken");
		horizontalPanel_1.add(hprlnkExtrato);
		hprlnkExtrato.setSize("100px", "15px");
		
		Hyperlink hyperlinkTransferencias = new Hyperlink("Realizar Transferência", false, "newHistoryToken");
		horizontalPanel_1.add(hyperlinkTransferencias);
		hyperlinkTransferencias.setSize("100px", "30px");
	}
}
