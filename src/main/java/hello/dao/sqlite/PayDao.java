package hello.dao.sqlite;

import hello.domain.sqlite.RequestAuthenInfo;

public interface PayDao{
	
	
	public void requestPhoneAuthen(RequestAuthenInfo requestAuthenInfo);

	public String getPrc(String inSynonymId);
}
