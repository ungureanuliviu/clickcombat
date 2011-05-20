package liviu.apps.clickcombat.data;

import android.os.Bundle;
import liviu.apps.clickcombat.interfaces.BundleAnnotation;

public class Domain{
	
	// constants
	private final String TAG		= "Domain";	
	
	// data
	@BundleAnnotation(bundleKey = "NAME")	
	private String	name;
	
	@BundleAnnotation(bundleKey = "PX")
	private float  px;
	
	@BundleAnnotation(bundleKey = "ID")
	private int		id;
	
	@BundleAnnotation (bundleKey = "USER_ID")
	private int 	user_id;
	
	@BundleAnnotation (bundleKey = "DESCRIPION")
	private String  description;
	
	@BundleAnnotation (bundleKey = "FILE")
	private String  file;
	
	@BundleAnnotation (bundleKey = "ADDED_DATE")
	private long	addedDate;
	
	@BundleAnnotation (bundleKey = "CLICKS")
	private int		clicks;
	
	@BundleAnnotation (bundleKey = "IS_VERIFIED")
	private boolean is_verified;	
	
	private BundleConvertor bundleConvertor;
	
	
	public Domain() {
		name 			= "no name";
		px 				= 0;
		id				= -1;
		user_id			= -1;
		description 	= " ";
		file			= "";
		addedDate		= 0;
		clicks			= 0;
		is_verified		= false;
		bundleConvertor = new BundleConvertor();
	}
	
	public Domain(int id_,
				  int user_id_,
			      String name_,
				  float px_,
				  String desc_,
				  String file_,
				  long addedDate_,
				  int clicks_,
				  boolean is_verified_){
		
		name 			= name_;
		px 				= px_;
		id				= id_;
		user_id			= user_id_;
		description 	= desc_;
		file			= file_;
		addedDate		= addedDate_;
		clicks			= clicks_;
		is_verified		= is_verified_;
		bundleConvertor = new BundleConvertor();
	}
	
	public long getAddedDate() {
		return addedDate;
	}
	
	public int getClicks() {
		return clicks;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getFile() {
		return file;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public float getPx() {
		return px;
	}
	
	public int getUserId() {
		return user_id;
	}
	
	public void setAddedDate(long addedDate) {
		this.addedDate = addedDate;
	}
	
	public void setClicks(int clicks) {
		this.clicks = clicks;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setFile(String file) {
		this.file = file;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setIsVerified(boolean is_verified) {
		this.is_verified = is_verified;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPx(float px) {
		this.px = px;
	}
	
	public void setUserId(int user_id) {
		this.user_id = user_id;
	}
	
	public Bundle toBundle(){
		return bundleConvertor.toBundle(this);
	}
}
