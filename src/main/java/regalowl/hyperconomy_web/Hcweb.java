package regalowl.hyperconomy_web;



import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import regalowl.hyperconomy.HyperConomy;
import regalowl.hyperconomy.LanguageFile;

public class Hcweb implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		HyperConomy hc = HyperConomy.hc;
		HyperConomy_Web hcw = HyperConomy_Web.hcw;
		LanguageFile L = hc.getLanguageFile();
		WebHandler wh = hcw.getWebHandler();
		try {
			if (args.length == 0) {
				sender.sendMessage(L.get("HCWEB_INVALID"));
				return true;
			}
			if (args[0].equalsIgnoreCase("enable")) {
				hcw.gYH().gFC("config").set("config.web-page.use-web-page", true);
				sender.sendMessage(L.get("WEB_PAGE_ENABLED"));
				hcw.restart();
			} else if (args[0].equalsIgnoreCase("disable")) {
				hcw.gYH().gFC("config").set("config.web-page.use-web-page", false);
				sender.sendMessage(L.get("WEB_PAGE_DISABLED"));
				hcw.restart();
			} else if (args[0].equalsIgnoreCase("background")) {
				hcw.gYH().gFC("config").set("config.web-page.background-color", args[1]);
				sender.sendMessage(L.get("WEB_PAGE_SET"));
				hcw.restart();
			} else if (args[0].equalsIgnoreCase("tabledata")) {
				hcw.gYH().gFC("config").set("config.web-page.table-data-color", args[1]);
				sender.sendMessage(L.get("WEB_PAGE_SET"));
				hcw.restart();
			} else if (args[0].equalsIgnoreCase("fontsize")) {
				hcw.gYH().gFC("config").set("config.web-page.font-size", args[1]);
				sender.sendMessage(L.get("WEB_PAGE_SET"));
				hcw.restart();
			} else if (args[0].equalsIgnoreCase("font")) {
				hcw.gYH().gFC("config").set("config.web-page.font", args[1]);
				sender.sendMessage(L.get("WEB_PAGE_SET"));
				hcw.restart();
			} else if (args[0].equalsIgnoreCase("port")) {
				hcw.gYH().gFC("config").set("config.web-page.port", Integer.parseInt(args[1]));
				sender.sendMessage(L.get("WEB_PAGE_SET"));
				hcw.restart();
			} else if (args[0].equalsIgnoreCase("economy")) {
				hcw.gYH().gFC("config").set("config.web-page.web-page-economy", args[1]);
				sender.sendMessage(L.get("WEB_PAGE_SET"));
				hcw.restart();
			} else if (args[0].equalsIgnoreCase("font")) {
				hcw.gYH().gFC("config").set("config.web-page.font-color", args[1]);
				sender.sendMessage(L.get("WEB_PAGE_SET"));
				hcw.restart();
			} else if (args[0].equalsIgnoreCase("border")) {
				hcw.gYH().gFC("config").set("config.web-page.border-color", args[1]);
				sender.sendMessage(L.get("WEB_PAGE_SET"));
				hcw.restart();
			} else if (args[0].equalsIgnoreCase("increase")) {
				hcw.gYH().gFC("config").set("config.web-page.increase-value-color", args[1]);
				sender.sendMessage(L.get("WEB_PAGE_SET"));
				hcw.restart();
			} else if (args[0].equalsIgnoreCase("decrease")) {
				hcw.gYH().gFC("config").set("config.web-page.decrease-value-color", args[1]);
				sender.sendMessage(L.get("WEB_PAGE_SET"));
				hcw.restart();
			} else if (args[0].equalsIgnoreCase("highlight")) {
				hcw.gYH().gFC("config").set("config.web-page.highlight-row-color", args[1]);
				sender.sendMessage(L.get("WEB_PAGE_SET"));
				hcw.restart();
			} else if (args[0].equalsIgnoreCase("header")) {
				hcw.gYH().gFC("config").set("config.web-page.header-color", args[1]);
				sender.sendMessage(L.get("WEB_PAGE_SET"));
				hcw.restart();
			} else if (args[0].equalsIgnoreCase("refresh")) {
				hcw.restart();
			} else if (args[0].equalsIgnoreCase("setdefault")) {
				hcw.gYH().gFC("config").set("config.web-page.background-color", "8FA685");
				hcw.gYH().gFC("config").set("config.web-page.font-color", "F2F2F2");
				hcw.gYH().gFC("config").set("config.web-page.border-color", "091926");
	    		hcw.gYH().gFC("config").set("config.web-page.increase-value-color", "C8D9B0");
	    		hcw.gYH().gFC("config").set("config.web-page.decrease-value-color", "F2B2A8");
	    		hcw.gYH().gFC("config").set("config.web-page.highlight-row-color", "8FA685");
	    		hcw.gYH().gFC("config").set("config.web-page.header-color", "091926");
	    		hcw.gYH().gFC("config").set("config.web-page.table-data-color", "314A59");
	    		hcw.gYH().gFC("config").set("config.web-page.font-size", 12);
	    		hcw.gYH().gFC("config").set("config.web-page.font", "verdana");
	    		hcw.gYH().gFC("config").set("config.web-page.port", 7777);
				hcw.restart();
	    		sender.sendMessage(L.get("WEB_PAGE_SET"));
			} else if (args[0].equalsIgnoreCase("status")) {
				if (wh.getServer() == null) {
					sender.sendMessage(L.get("SERVER_NULL"));
				} else if (wh.getServer().isStopping()) {
					sender.sendMessage(L.get("SERVER_STOPPING"));
				} else  if (wh.getServer().isStarting()) {
					sender.sendMessage(L.get("SERVER_STARTING"));
				} else  if (wh.getServer().isFailed()) {
					sender.sendMessage(L.get("SERVER_FAILED"));
				} else  if (wh.getServer().isStopped()) {
					sender.sendMessage(L.get("SERVER_STOPPPED"));
				} else  if (wh.getServer().isRunning()) {
					sender.sendMessage(L.get("SERVER_RUNNING"));
				}
			} else {
				sender.sendMessage(L.get("HCWEB_INVALID"));
			}
			return true;
		} catch (Exception e) {
			hcw.getDataBukkit().writeError(e);
			sender.sendMessage(L.get("HCWEB_INVALID"));
			return true;
		}
	}
}
