package com.cafe.project.serviceImpl;
import com.cafe.project.POJO.User;
import com.cafe.project.constents.CafeConstants;
import com.cafe.project.dao.UserDao;
import com.cafe.project.service.UserService;
import com.cafe.project.utils.CafeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Objects;

@Service

public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
       log.info("Inside signup{}", requestMap);
       try{
       if(validateSignUpMap(requestMap)){
           User user=userDao.findByEmailId(requestMap.get("email"));
           if (Objects.isNull(user)){
                userDao.save(getUserFromMap(requestMap));
                return CafeUtils.getResponseEntity("Successfully Registered",HttpStatus.OK);
           }else{
               return CafeUtils.getResponseEntity("email already exit",HttpStatus.BAD_REQUEST);
           }
       }else{
           return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
       }
       }catch (Exception e){
           e.printStackTrace();
       }
       return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);


    }
    private boolean validateSignUpMap(Map<String,String> requestMap){
       if( requestMap.containsKey("name")&& requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email")&&requestMap.containsKey("password")){
           return true;

       }else{
           return false;
       }
    }
    private User getUserFromMap(Map<String,String> requestMap){
        User user=new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;

    }
}
