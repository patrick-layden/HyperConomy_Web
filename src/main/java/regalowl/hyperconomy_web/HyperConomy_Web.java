package regalowl.hyperconomy_web;


import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import regalowl.databukkit.DataBukkit;
import regalowl.databukkit.file.YamlHandler;
import regalowl.hyperconomy.HyperConomy;
import regalowl.hyperconomy.event.DataLoadListener;
import regalowl.hyperconomy.event.HyperEventHandler;


public class HyperConomy_Web extends JavaPlugin implements DataLoadListener {

	private HyperConomy hc;
	public static HyperConomy_Web hcw;
	private HyperEventHandler heh;
	private DataBukkit db;
	private YamlHandler yh;
	private WebHandler wh;
	private Logger log;
	
	
	

	private String backgroundColor;
	private String fontColor;
	private String borderColor;
	private String increaseColor;
	private String decreaseColor;
	private String highlightColor;
	private String headerColor;
	private String tableDataColor;
	private String font;
	private int fontSize;
	private int port;
	
	private String webAPIPath;
	private boolean useWebAPI;
	private boolean enabled;
	
	@Override
	public void onEnable() {
		enable();
	}
	@Override
	public void onDisable() {
		disable();
	}
	
	public void restart() {
		disable();
		enable();
		buildData();
	}
	
	private void enable() {
		enabled = false;
		hcw = this;
		hc = HyperConomy.hc;
		log = Logger.getLogger("Minecraft");
		heh = hc.getHyperEventHandler();
		heh.registerListener(this);
		registerCommands();
		db = new DataBukkit(this);
		yh = db.getYamlHandler();
		yh.copyFromJar("config");
		yh.registerFileConfiguration("config");
		yh.setCurrentFileConfiguration("config");
		yh.registerDefault("enable-web-api", false);
		yh.registerDefault("web-api-path", "API");
	}


	private void buildData() {
		if (hc.enabled()) {
			FileConfiguration config = yh.gFC("config");
			backgroundColor = "#" + config.getString("background-color");
			fontColor = "#" + config.getString("font-color");
			borderColor = "#" + config.getString("border-color");
			backgroundColor = "#" + config.getString("background-color");
			increaseColor = "#" + config.getString("increase-value-color");
			decreaseColor = "#" + config.getString("decrease-value-color");
			highlightColor = "#" + config.getString("highlight-row-color");
			headerColor = "#" + config.getString("header-color");
			tableDataColor = "#" + config.getString("table-data-color");
			font = config.getString("font");
			fontSize = config.getInt("font-size");
			port = config.getInt("port");
			webAPIPath = config.getString("web-api-path");
			useWebAPI = config.getBoolean("enable-web-api");
			if (wh == null) {
				wh = new WebHandler();
			}
			if (!wh.serverStarted()) {
				wh.startServer();
			}
			enabled = true;
		}
	}

	public void disable() {
		if (wh != null) {
			wh.endServer();
			wh = null;
		}
		if (db != null) {
			db.shutDown();
			db = null;
		}
		getServer().getScheduler().cancelTasks(this);
	}
	
	public void onDataLoad() {
		if (!enabled) {
			buildData();
		} else {
			restart();
		}
	}
	


	private void registerCommands() {
		Bukkit.getServer().getPluginCommand("hcweb").setExecutor(new Hcweb());
	}

	public WebHandler getWebHandler() {
		return wh;
	}
	
	public DataBukkit getDataBukkit() {
		return db;
	}
	
	public YamlHandler gYH() {
		return yh;
	}
	
	public Logger getLog() {
		return log;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public String getFontColor() {
		return fontColor;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public String getIncreaseColor() {
		return increaseColor;
	}

	public String getDecreaseColor() {
		return decreaseColor;
	}

	public String getHighlightColor() {
		return highlightColor;
	}

	public String getHeaderColor() {
		return headerColor;
	}

	public String getTableDataColor() {
		return tableDataColor;
	}

	public String getFont() {
		return font;
	}

	public int getFontSize() {
		return fontSize;
	}
	
	public int getPort() {
		return port;
	}
	
	public boolean useWebAPI() {
		return useWebAPI;
	}
	
	public String getWebAPIPath() {
		return webAPIPath;
	}

}
