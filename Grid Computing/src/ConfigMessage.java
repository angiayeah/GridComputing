

//this class intend to standarlize the communication between GS/RM/Nodes
public class ConfigMessage implements java.io.Serializable 
{

	ConfigMessageType type;   //the type of the Message
	MemberType target;       //the type of the target: GS/RM/Node
	int target_id;           //the id of the target
	String target_url;        //url of the target
	MemberType source;        //type of the event source, now it is GSchecker
	int source_id;            //id of the event source
	int time_to_live =1;      //like the time to live in IP packet, is of no use currently.
	public ConfigMessage(ConfigMessageType type,MemberType target, int target_id,String target_url,MemberType source,int source_id)
	{
		this.type = type;
		this.target = target;
		this.target_id = target_id;
		this.target_url = target_url;
		this.source = source;
		this.source_id = source_id;
	}
	
	public ConfigMessageType getType()
	{
		return this.type;
	}
	
	public MemberType getTargetType()
	{
		return this.target;
	}
	
	public int getTargetID()
	{
		return this.target_id;
	}
	
	public String getTargetURL()
	{
		return this.target_url;
	}
	
	public MemberType getSourceType()
	{
		return source;
	}
	
	public int getSourceID()
	{
		return source_id;
	}
	
	public void setSourceType(MemberType source)
	{
		this.source = source;
	}
	
	public void setSourceID(int source_id)
	{
		this.source_id = source_id;
	}
	
	public int getTimeToLive()
	{
		return time_to_live;
	}
	
	public void setTimeToLive(int TTL)
	{
		this.time_to_live = TTL;
	}
}
