package Utility;

public class ModelCSV {

	private String data;
    private String exception;
    private String productDomain;
    private String Date;
   
	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getProductDomain() 
	{
		return productDomain;
	}

	public void setProductDomain(String productDomain) {
		this.productDomain = productDomain;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

    
}
