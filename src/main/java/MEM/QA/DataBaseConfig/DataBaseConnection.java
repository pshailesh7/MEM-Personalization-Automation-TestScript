package MEM.QA.DataBaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class DataBaseConnection {
	
	public static Connection getDbConnection() throws Exception {
		try {

			String jdbcUrl = "jdbc:mysql://localhost:3306/myeasymonogram1";
			String user = "root";
			String password = "";
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
			return connection;

		} catch (Exception e) {
			throw e;
		}
	}

	public List<Store> getStoreList() throws Exception {
		try  {
//			UrlData response = new UrlData();
			
			Statement statement = getDbConnection().createStatement();

			ResultSet resultSet = statement.executeQuery(
					"select su.app_store_id,su.myshopify_domain from shopify_users su where su.is_deleted ='0' and status = '1'");

			List<Store> storeList = new ArrayList<Store>();
			
			while (resultSet.next()) {
				Store store = new Store();
				Long data = resultSet.getLong("app_store_id");
				String data1 = resultSet.getString("myshopify_domain");
				store.setAppStoreId(data);
				store.setDomain(data1);
				storeList.add(store);
			}
			return storeList;
//			
		} catch (Exception e) {
			throw e;
		}
	}
	public List<MerchantProduct> getMerchantProducts(Store store, Statement statement,int pageSize, int offset) throws SQLException {
		List<MerchantProduct> merchantproductlist = new ArrayList<MerchantProduct>();

			ResultSet resultSetlist = statement.executeQuery(
					"select mp.master_product_id,mp.title from merchant_products mp where mp.app_store_id = "
							+ store.getAppStoreId() + " and store_product_id is not null LIMIT " + pageSize +" OFFSET "+offset);
			while (resultSetlist.next()) {
				MerchantProduct merchantProduct = new MerchantProduct();
				Long masterProduct = resultSetlist.getLong("master_product_id");
				String title = resultSetlist.getString("title");
				merchantProduct.setMasteProductId(masterProduct);
				merchantProduct.setTitle(title);
				merchantproductlist.add(merchantProduct);
		}
			return merchantproductlist;
	}

	public List<String> getMasterProducts(List<MerchantProduct> merchantproductlist, Statement statement, Store store,int pageSize,int offset)
			throws SQLException {
		
		List<String> urls = new ArrayList<String>();
		for (MerchantProduct merchantProduct : merchantproductlist) {

			ResultSet masterProductset = statement
					.executeQuery("select * from master_products mp where mp.master_product_id = "
							+ merchantProduct.getMasteProductId() + " and mp.customize_text IS NOT NULL and CHAR_LENGTH(mp.customize_text) >5 LIMIT " + pageSize +" OFFSET "+offset);

			if (masterProductset.next()) {
				String stringformatedurl = merchantProduct.getTitle().replace(" - ", " ");
				String finalurl = stringformatedurl.replace(" ", "-");
				finalurl = finalurl.toLowerCase();
				String url = "https://"+store.getDomain() + "/products/" + finalurl;
				urls.add(url);
			}
		}
		return urls;
	}
}
