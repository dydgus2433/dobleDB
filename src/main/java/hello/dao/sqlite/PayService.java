package hello.dao.sqlite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hello.domain.sqlite.RequestAuthenInfo;

@Service("payService")
public class PayService {

	@Autowired
	private PayDao payDao;
	
	public RequestAuthenInfo requestAuthenInfo(RequestAuthenInfo requestAuthenInfo) {
		String inPgEpayAcnt = payDao.getPrc("");
		payDao.requestPhoneAuthen(requestAuthenInfo);
		return requestAuthenInfo;
	}
	
	public String getPrc(String str) {
		return payDao.getPrc(str);
	}
}
