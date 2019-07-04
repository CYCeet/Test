package Web;

public class LoginPanDuan {

//	private HashMap<String, String> bodyMap = new HashMap<>();
	
	private LoginPanDuan() {
		
	}
	/*private LoginPanDuan(HashMap<String, String> bodyMap) throws Exception {
		this.bodyMap = bodyMap;
	}*/

	public boolean select(String name,String id) {
		if ("zs".equals(name) && "123".equals(id)) {
			return true;
		} else {
			return false;
		}
		
	}
	
	/*public HashMap<String, String> getBodyMap(){
		return  bodyMap;
	}*/
	
	/*public void setBodyMap(HashMap<String, String> bodyMap) {
		this.bodyMap = bodyMap;
	}*/
	
	
}
