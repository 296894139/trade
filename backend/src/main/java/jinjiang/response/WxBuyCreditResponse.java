package jinjiang.response;

public class WxBuyCreditResponse extends Response {
	private WxBuyCreditItem wxBuyCredit;

	public WxBuyCreditResponse(WxBuyCreditItem wxBuyCredit) {
		this.wxBuyCredit = wxBuyCredit;
	}

	public WxBuyCreditItem getWxBuyCredit() {
		return wxBuyCredit;
	}

	public void setWxBuyCredit(WxBuyCreditItem wxBuyCredit) {
		this.wxBuyCredit = wxBuyCredit;
	}
}
