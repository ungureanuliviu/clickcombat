package liviu.apps.clickcombat.data;

public class Domain {
	
	// constants
	private final String TAG		= "Domain";
	
	/*
	 *         "dm_id": "1",
        "dm_name": "http:\/\/www.kinno.me",
        "dm_user_id": "1",
        "dm_description": "my first website",
        "dm_file_check": "552a649a3917d306763b7866680ae1a1.html",
        "dm_added_date": "1304796938",
        "dm_clicks": "18",
        "dm_is_verified": "1",
        "px": 18
	 */
	
	// data
	private String	name;
	private double  px;
	private int		id;
	private int 	user_id;
	private String  description;
	private String  file;
	private long	addedDate;
	private int		clicks;
	private boolean is_verified;	
	
	
	public Domain() {
		name 		= "no name";
		px 			= 0;
		id			= -1;
		user_id		= -1;
		description = " ";
		file		= "";
		addedDate	= 0;
		clicks		= 0;
		is_verified	= false;
	}
	
	public Domain(int id_,
				  int user_id_,
			      String name_,
				  double px_,
				  String desc_,
				  String file_,
				  long addedDate_,
				  int clicks_,
				  boolean is_verified_){
		
		name 		= name_;
		px 			= px_;
		id			= id_;
		user_id		= user_id_;
		description = desc_;
		file		= file_;
		addedDate	= addedDate_;
		clicks		= clicks_;
		is_verified	= is_verified_;
		
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
	
	public double getPx() {
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
	
	public void setPx(double px) {
		this.px = px;
	}
	
	public void setUserId(int user_id) {
		this.user_id = user_id;
	}
	
}
