package regalowl.hyperconomy_web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import regalowl.databukkit.CommonFunctions;
import regalowl.hyperconomy.EnchantmentClass;
import regalowl.hyperconomy.History;
import regalowl.hyperconomy.HyperConomy;
import regalowl.hyperconomy.HyperEnchant;
import regalowl.hyperconomy.HyperItem;
import regalowl.hyperconomy.HyperObject;
import regalowl.hyperconomy.HyperObjectStatus;
import regalowl.hyperconomy.HyperXP;
import regalowl.hyperconomy.PlayerShop;
import regalowl.hyperconomy.PlayerShopObject;
import regalowl.hyperconomy.Shop;

public class ShopPage extends HttpServlet {

	private static final long serialVersionUID = 699465359999143309L;
	private HyperConomy_Web hcw;
	private HyperConomy hc;
	private CommonFunctions cf;
	private History hist;
	private Shop s;
	private String page = "Loading...";

	public ShopPage(Shop shop) {
		hcw = HyperConomy_Web.hcw;
		hc = HyperConomy.hc;
		cf = hcw.getDataBukkit().getCommonFunctions();
		hist = hc.getHistory();
		s = shop;
		page = buildLoadPage();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(page);
	}

	public void updatePage() {
		hcw.getServer().getScheduler().runTaskAsynchronously(hcw, new Runnable() {
			public void run() {
				page = buildPage(s.getEconomy());
			}
		});
	}

	private String buildPage(String economy) {
		try {
			String page = "";
			if (s == null) {
				return "";
			}
			PlayerShop ps = null;
			if (s instanceof PlayerShop) {
				ps = (PlayerShop) s;
			}
			boolean useHistory = hist.useHistory();
			if (ps != null) {
				useHistory = false;
			}
			ArrayList<HyperObject> objects = s.getAvailableObjects();
			Collections.sort(objects);

			HashMap<HyperObject, String> hour = null;
			HashMap<HyperObject, String> sixHours = null;
			HashMap<HyperObject, String> day = null;
			HashMap<HyperObject, String> threeDay = null;
			HashMap<HyperObject, String> week = null;
			if (useHistory) {
				hour = hist.getPercentChange(economy, 1);
				sixHours = hist.getPercentChange(economy, 6);
				day = hist.getPercentChange(economy, 24);
				threeDay = hist.getPercentChange(economy, 72);
				week = hist.getPercentChange(economy, 168);
			}
			page += "<html>\n";
			page += "<head>\n";
			page += "<script type='text/javascript'>\n";
			page += "</script>\n";
			page += "<style>\n";
			page += "* {font-family:" + hcw.getFont() + ";font-size:" + hcw.getFontSize() + "px;color:" + hcw.getFontColor() + ";}\n";
			page += "body {background:" + hcw.getBackgroundColor() + ";}\n";
			page += "td {vertical-align:top;border:1px solid " + hcw.getBorderColor() + ";background:" + hcw.getTableDataColor() + ";}\n";
			page += "td.red {vertical-align:top;border:1px solid " + hcw.getBorderColor() + ";background:" + hcw.getDecreaseColor() + ";}\n";
			page += "td.green {vertical-align:top;border:1px solid " + hcw.getBorderColor() + ";background:" + hcw.getIncreaseColor() + ";}\n";
			page += "th {border:1px solid " + hcw.getBorderColor() + ";padding:3px;cursor:pointer;}\n";
			page += "th.header {background:" + hcw.getHeaderColor() + ";}\n";
			page += "tr:hover {background:" + hcw.getHighlightColor() + ";}\n";
			page += "td:hover {background:" + hcw.getHighlightColor() + ";}\n";
			page += "</style>\n";

			page += "</head>\n";
			page += "<body>\n";
			page += "<div align='center' id='results'>\n";
			page += "<TABLE BORDER='0'>\n";

			page += "<TR>\n";
			page += "<TH class='header'>\n";
			page += "Name\n";
			page += "</TH>\n";
			page += "<TH class='header'>\n";
			page += "Sell\n";
			page += "</TH>\n";
			page += "<TH class='header'>\n";
			page += "Buy\n";
			page += "</TH>\n";
			page += "<TH class='header'>\n";
			page += "Stock\n";
			page += "</TH>\n";
			page += "<TH class='header'>\n";
			page += "ID\n";
			page += "</TH>\n";

			if (useHistory) {
				page += "<TH class='header'>\n";
				page += "1 Hour\n";
				page += "</TH>\n";
				page += "<TH class='header'>\n";
				page += "6 Hour\n";
				page += "</TH>\n";
				page += "<TH class='header'>\n";
				page += "1 Day\n";
				page += "</TH>\n";
				page += "<TH class='header'>\n";
				page += "3 Days\n";
				page += "</TH>\n";
				page += "<TH class='header'>\n";
				page += "1 Week";
				page += "</TH>\n";
			}
			page += "</TR>\n";

			for (HyperObject ho : objects) {
				PlayerShopObject pso = null;
				HyperObjectStatus hos = null;
				if (ho instanceof PlayerShopObject) {
					pso = (PlayerShopObject) ho;
					hos = pso.getStatus();
					if (hos == HyperObjectStatus.NONE) {
						continue;
					}
				}
				if (!hc.enabled()) {
					return "";
				}

				double sellPrice = -1;
				double buyPrice = -1;
				String buyString = "";
				String sellString = "";
				if (ho instanceof HyperItem) {
					HyperItem hi = (HyperItem) ho;
					sellPrice = hi.getValue(1);
					sellPrice -= hi.getSalesTaxEstimate(sellPrice);
					buyPrice = hi.getCost(1);
					buyPrice += hi.getPurchaseTax(buyPrice);
					buyString = hc.getLanguageFile().fC(cf.twoDecimals(buyPrice));
					sellString = hc.getLanguageFile().fC(cf.twoDecimals(sellPrice));
				} else if (ho instanceof HyperEnchant) {
					HyperEnchant he = (HyperEnchant) ho;
					sellPrice = he.getValue(EnchantmentClass.DIAMOND);
					sellPrice -= he.getSalesTaxEstimate(sellPrice);
					buyPrice = he.getCost(EnchantmentClass.DIAMOND);
					buyPrice += he.getPurchaseTax(buyPrice);
					buyString = hc.getLanguageFile().fC(cf.twoDecimals(buyPrice));
					sellString = hc.getLanguageFile().fC(cf.twoDecimals(sellPrice));
				} else if (ho instanceof HyperXP) {
					HyperXP xp = (HyperXP) ho;
					sellPrice = xp.getValue(1);
					sellPrice -= xp.getSalesTaxEstimate(sellPrice);
					buyPrice = xp.getCost(1);
					buyPrice += xp.getPurchaseTax(buyPrice);
					buyString = hc.getLanguageFile().fC(cf.twoDecimals(buyPrice));
					sellString = hc.getLanguageFile().fC(cf.twoDecimals(sellPrice));
				}
				if (hos != null) {
					if (hos == HyperObjectStatus.BUY) {
						sellString = "N/A";
					} else if (hos == HyperObjectStatus.SELL) {
						buyString = "N/A";
					}
				}
				page += "<TR>\n";
				page += "<TD>\n";
				page += ho.getName() + "\n";
				page += "</TD>\n";
				page += "<TD>\n";
				page += sellString + "\n";
				page += "</TD>\n";
				page += "<TD>\n";
				page += buyString + "\n";
				page += "</TD>\n";
				page += "<TD>\n";
				page += cf.twoDecimals(ho.getStock()) + "\n";
				page += "</TD>\n";
				page += "<TD>\n";

				int id = -1;
				if (ho instanceof HyperItem) {
					HyperItem hi = (HyperItem) ho;
					id = hi.getId();
				}

				page += id + "\n";
				page += "</TD>\n";

				if (useHistory) {
					String pc = hour.get(ho);
					String iclass = "";
					if (pc.indexOf("-") != -1) {
						iclass = "red";
					} else if (pc.indexOf("?") != -1 || pc.equalsIgnoreCase("0.0")) {
						iclass = "none";
					} else {
						iclass = "green";
					}
					page += "<TD " + "class='" + iclass + "'>\n";
					page += hour.get(ho) + "%\n";
					page += "</TD>\n";

					pc = sixHours.get(ho);
					iclass = "";
					if (pc.indexOf("-") != -1) {
						iclass = "red";
					} else if (pc.indexOf("?") != -1 || pc.equalsIgnoreCase("0.0")) {
						iclass = "none";
					} else {
						iclass = "green";
					}
					page += "<TD " + "class='" + iclass + "'>\n";
					page += sixHours.get(ho) + "%\n";
					page += "</TD>\n";

					pc = day.get(ho);
					iclass = "";
					if (pc.indexOf("-") != -1) {
						iclass = "red";
					} else if (pc.indexOf("?") != -1 || pc.equalsIgnoreCase("0.0")) {
						iclass = "none";
					} else {
						iclass = "green";
					}
					page += "<TD " + "class='" + iclass + "'>\n";
					page += day.get(ho) + "%\n";
					page += "</TD>\n";

					pc = threeDay.get(ho);
					iclass = "";
					if (pc.indexOf("-") != -1) {
						iclass = "red";
					} else if (pc.indexOf("?") != -1 || pc.equalsIgnoreCase("0.0")) {
						iclass = "none";
					} else {
						iclass = "green";
					}
					page += "<TD " + "class='" + iclass + "'>\n";
					page += threeDay.get(ho) + "%\n";
					page += "</TD>\n";

					pc = week.get(ho);
					iclass = "";
					if (pc.indexOf("-") != -1) {
						iclass = "red";
					} else if (pc.indexOf("?") != -1 || pc.equalsIgnoreCase("0.0")) {
						iclass = "none";
					} else {
						iclass = "green";
					}
					page += "<TD " + "class='" + iclass + "'>\n";
					page += week.get(ho) + "%\n";
					page += "</TD>\n";
				}
			}

			page += "</TABLE>\n";
			page += "</div>\n";
			page += "</body>\n";
			page += "</html>\n";

			return page;
		} catch (Exception e) {
			//e.printStackTrace();
			return "This page didn't load properly.  Please wait for it to reload.";
		}
	}

	private String buildLoadPage() {
		String page = "";
		page += "<html>\n";
		page += "<head>\n";
		page += "<script type='text/javascript'>\n";
		page += "</script>\n";
		page += "<style>\n";
		page += "* {font-family:" + hcw.getFont() + ";font-size:" + hcw.getFontSize() + "px;color:" + hcw.getFontColor() + ";}\n";
		page += "body {background:" + hcw.getBackgroundColor() + ";}\n";
		page += "td {vertical-align:top;border:1px solid " + hcw.getBorderColor() + ";background:" + hcw.getTableDataColor() + ";}\n";
		page += "td.red {vertical-align:top;border:1px solid " + hcw.getBorderColor() + ";background:" + hcw.getDecreaseColor() + ";}\n";
		page += "td.green {vertical-align:top;border:1px solid " + hcw.getBorderColor() + ";background:" + hcw.getIncreaseColor() + ";}\n";
		page += "th {border:1px solid " + hcw.getBorderColor() + ";padding:3px;cursor:pointer;}\n";
		page += "th.header {background:" + hcw.getHeaderColor() + ";}\n";
		page += "tr:hover {background:" + hcw.getHighlightColor() + ";}\n";
		page += "td:hover {background:" + hcw.getHighlightColor() + ";}\n";
		page += "</style>\n";
		page += "</head>\n";
		page += "<body>\n";
		page += "<div align='center' id='results'>\n";
		page += "<TABLE BORDER='0'>\n";
		page += "<TR>\n";
		page += "<TH class='header'>\n";
		page += "...HyperConomy Loading...\n";
		page += "</TH>\n";
		page += "</TR>\n";
		page += "</TABLE>\n";
		page += "</div>\n";
		page += "</body>\n";
		page += "</html>\n";
		return page;
	}
}
