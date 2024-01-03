package MEM.QA.DataBaseConfig;

public class MerchantProduct {
private long masterProductId;
private long merchantProductId;
	
	private String title;

	public long getMerchantProductId() {
		return merchantProductId;
	}

	public void setMerchantProductId(long merchantProductId) {
		this.merchantProductId = merchantProductId;
	}

	public long getMasteProductId() {
		return masterProductId;
	}

	public void setMasteProductId(long masteProductId) {
		this.masterProductId = masteProductId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
