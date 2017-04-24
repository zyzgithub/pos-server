package jeecg.system.service.impl;

import jeecg.system.pojo.base.TSUser;
import jeecg.system.service.UserService;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("userService")
@Transactional
public class UserServiceImpl extends CommonServiceImpl implements UserService {

	public TSUser checkUserExits(TSUser user){
		return this.commonDao.getUserByUserIdAndUserNameExits(user);
	}
	
	public String getUserRole(TSUser user){
		return this.commonDao.getUserRole(user);
	}
	
	@Override
	public void pwdInit(TSUser user,String newPwd) {
			this.commonDao.pwdInit(user,newPwd);
	}
	
	@Override
	public boolean existUser(String mobile) {
		String sql = "select count(*) from user where mobile = ? and user_type = 'merchant'";
		int count =findOneForJdbc(sql, Integer.class, mobile);
		
		return count > 0;
	}
}
