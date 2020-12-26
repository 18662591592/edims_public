package com.bgi.edims.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.bgi.edims.model.EdiUser;

@Mapper
public interface UserMapper {
	public int createEdiUser(EdiUser ediUser);
	public int deleteEdiUser(EdiUser ediUser);
	public int updateEdiUser(EdiUser ediUser);
	public List<EdiUser> getEdiUsers(EdiUser ediUser);
	public List<EdiUser> getBaseEdiUsers(EdiUser ediUser);
	public Integer getEdiUsersCount(EdiUser ediUser);
	public Integer getBaseEdiUsersCount(EdiUser ediUser);
}
