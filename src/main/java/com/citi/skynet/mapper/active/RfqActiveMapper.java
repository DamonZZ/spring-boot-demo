package com.citi.skynet.mapper.active;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.citi.skynet.common.model.RfqHeader;
import com.citi.skynet.common.model.RfqProperty;

@Mapper
public interface RfqActiveMapper {
	@Select("SELECT id,rfq_id,version,parent_id,flow,message_id,state,sent_by,event,timestamp,roledefinition,routingtargets FROM saas_rfqhdr")
	@Results({ @Result(column = "id", property = "id"), @Result(column = "rfq_id", property = "rfqId"),
			@Result(column = "version", property = "version"), @Result(column = "parent_id", property = "parentId"),
			@Result(column = "flow", property = "flow"), @Result(column = "message_id", property = "messageId"),
			@Result(column = "state", property = "state"), @Result(column = "sent_by", property = "sentBy"),
			@Result(column = "event", property = "event"), @Result(column = "timestamp", property = "timestamp"),
			@Result(column = "roledefinition", property = "roledefinition"),
			@Result(column = "routingtargets", property = "routingtargets") })
	RfqHeader selectRfqHeader();

	@Select("SELECT id,rfq_id,version,parent_id,flow,message_id,state,sent_by,event,timestamp,roledefinition,routingtargets FROM saas_rfqhdr where rfq_id=#{rfqId}")
	@Results({ @Result(column = "id", property = "id"), @Result(column = "rfq_id", property = "rfqId"),
			@Result(column = "version", property = "version"), @Result(column = "parent_id", property = "parentId"),
			@Result(column = "flow", property = "flow"), @Result(column = "message_id", property = "messageId"),
			@Result(column = "state", property = "state"), @Result(column = "sent_by", property = "sentBy"),
			@Result(column = "event", property = "event"), @Result(column = "timestamp", property = "timestamp"),
			@Result(column = "roledefinition", property = "roledefinition"),
			@Result(column = "routingtargets", property = "routingtargets") })
	RfqHeader selectRfqHeaderByRfqId(@Param("rfqId") String rfqId);

	@Select("SELECT id,rfq_id,version,name,value FROM saas_rfqdtls where rfq_id=#{rfqId}")
	@Results({ @Result(column = "id", property = "id"), @Result(column = "rfq_id", property = "rfqId"),
			@Result(column = "version", property = "version"), @Result(column = "name", property = "name"),
			@Result(column = "value", property = "value") })
	List<RfqProperty> selectRfqPropertiesByRfqId(@Param("rfqId") String rfqId);

	@Insert("INSERT INTO saas_rfqhdr(id,rfq_id,version,parent_id,flow,message_id,state,sent_by,event,timestamp,roledefinition,routingtargets) VALUES(#{id},#{rfqId},#{version},#{parentId},#{flow},#{messageId},#{state},#{sentBy},#{event},#{timestamp},#{roledefinition},#{routingtargets})")
	Integer insertRfqHeader(RfqHeader header);

	@Insert("<script>INSERT INTO saas_rfqdtls(id,rfq_id,version,name,value) VALUES"
			+ "<foreach collection='list' item='rfqProperty' index='index' separator=','>(#{rfqProperty.id},#{rfqProperty.rfqId},#{rfqProperty.version},#{rfqProperty.name},#{rfqProperty.value})</foreach></script>")
	Integer insertRfqProperties(List<RfqProperty> rfqProperties);

	@Update("UPDATE saas_rfqhdr SET version=#{version},parent_id=#{parentId},flow=#{flow},message_id=#{messageId},state=#{state},sent_by=#{sentBy},event=#{event},timestamp=#{timestamp},roledefinition=#{roledefinition},routingtargets=#{routingtargets} where rfq_id=#{rfqId}")
	int updateRfqHeader(RfqHeader rfqHeader);

	@Update({ "<script>", "<foreach collection='list' item='rfqProperty' index='index' separator=','>",
			"UPDATE saas_rfqdtls",
			"SET(version=#{rfqProperty.version}, value=#{rfqProperty.value} where rfq_id=#{rfqProperty.rfqId} and name=#{rfqProperty.name})",
			"</foreach>", "</script>" })
	int updateRfqProerties(List<RfqProperty> rfqProperties);

	@Update("UPDATE saas_rfqdtls SET version=#{version}, value=#{value} where rfq_id=#{rfqId} and name=#{name}")
	int updateRfqProerty(RfqProperty rfqProperty);
	
	@Insert("INSERT INTO saas_rfqdtls(id,rfq_id,version,name,value) VALUES(#{id},#{rfqId},#{version},#{name},#{value})")
	int insertRfqProperty(RfqProperty rfqProperty);	
}
