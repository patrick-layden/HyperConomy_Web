package regalowl.hyperconomy_web;


import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.scheduler.BukkitTask;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import regalowl.hyperconomy.HyperConomy;
import regalowl.hyperconomy.Shop;
import regalowl.hyperconomy.ShopCreationListener;

public class WebHandler implements ShopCreationListener {

	private HyperConomy hc;
	private HyperConomy_Web hcw;
	private BukkitTask updateTask;
	private Server server;
	private ServletContextHandler context;
	private ArrayList<ShopPage> shopPages = new ArrayList<ShopPage>();
	private Shop s;
	private AtomicBoolean serverStarted = new AtomicBoolean();

	WebHandler() {
		hc = HyperConomy.hc;
		hcw = HyperConomy_Web.hcw;
		hc.getHyperEventHandler().registerShopCreationListener(this);
		serverStarted.set(false);
	}
	

	public void startServer() {
		try {
			hcw.getServer().getScheduler().runTaskAsynchronously(hcw, new Runnable() {
				public void run() {
					System.setProperty("org.eclipse.jetty.LEVEL", "WARN");
					server = new Server(hcw.getPort());
					context = new ServletContextHandler(ServletContextHandler.SESSIONS);
					context.setContextPath("/");
					server.setHandler(context);
					// context.addServlet(new ServletHolder(new HyperWebAPI()), "/API/*");
					context.addServlet(new ServletHolder(new MainPage()), "/");
					for (Shop s : hc.getEconomyManager().getShops()) {
						ShopPage sp = new ShopPage(s);
						shopPages.add(sp);
						context.addServlet(new ServletHolder(sp), "/" + s.getName() + "/*");
					}
					try {
						server.start();
						server.join();
					} catch (Exception e) {
						hcw.getDataBukkit().writeError(e);
					}
					serverStarted.set(true);
				}
			});
			updateTask = hcw.getServer().getScheduler().runTaskTimerAsynchronously(hcw, new Runnable() {
				public void run() {
					try {
						for (ShopPage sp:shopPages) {
							sp.updatePage();
						}
					} catch (Exception e) {
						hcw.getDataBukkit().writeError(e);
					}
				}
			}, 400L, 6000L);
			hcw.getLog().info("[HyperConomy_Web]Web server enabled.  Running on port " + hcw.getPort() + ".");
		} catch (Exception e) {
			hcw.getDataBukkit().writeError(e);;
		}
	}
	
	
	public void updatePages() {
		hcw.getServer().getScheduler().runTaskAsynchronously(hcw, new Runnable() {
			public void run() {
				try {
					for (ShopPage sp:shopPages) {
						sp.updatePage();
					}
				} catch (Exception e) {
					hcw.getDataBukkit().writeError(e);
				}
			}
		});
	}
	
	public void onShopCreation(Shop s) {
		addShop(s);
	}
	
	public void addShop(Shop shop) {
		s = shop;
		hcw.getServer().getScheduler().runTaskAsynchronously(hcw, new Runnable() {
			public void run() {
				ShopPage sp = new ShopPage(s);
				shopPages.add(sp);
				context.addServlet(new ServletHolder(sp), "/" + s.getName() + "/*");
			}
		});
	}
	
	

	public void endServer() {
		if (updateTask != null) {
			updateTask.cancel();
		}
		if (context != null) {
			try {
				context.stop();
				if (!context.isStopped()) {
					hcw.getDataBukkit().writeError("Context failed to stop.");
				}
			} catch (Exception e) {
				hcw.getDataBukkit().writeError(e);
			}
		}
		if (server != null) {
			try {
				server.stop();
				if (!server.isStopped()) {
					hcw.getDataBukkit().writeError("Server failed to stop.");
				}
			} catch (Exception e) {
				hcw.getDataBukkit().writeError(e);
			}
		}
	}
	
	public Server getServer() {
		return server;
	}
	
	public boolean serverStarted() {
		return serverStarted.get();
	}




}