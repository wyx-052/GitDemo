package cn.edu.scujcc.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import cn.edu.scujcc.dao.ChannelRepository;
import cn.edu.scujcc.model.Channel;;


@Service
public class ChannelService {
	@Autowired
	private ChannelRepository repo;
	
	
	/*
	 * 获取所有频道的数据
	 * @return 频道List*/
	public List<Channel> getAllChannels(){
		return repo.findAll();
	}
	
	/*
	 * 获取一个频道的数据
	 * @param channelId 频道编号
	 * @return 频道对象，若未找到则返回null
	 * */
	public Channel getChannel(String channelId) {
		Optional<Channel> result = repo.findById(channelId);
		
		if (result.isPresent()) {
			return result.get();
		}else {
			return null;
		}
	}
	
	/*
	 * 删除指定的频道
	 * @param channelId 待删除的频道编号
	 * @return 若删除成功则返回true，否则返回false
	 * */
	public boolean deleteChannel(String channelId) {
		boolean result = true;
		repo.deleteById(channelId);
		return result;
	}
	
	/*
	 * 保存频道
	 * @param c 待保存的频道对象（没有id值）
	 * @return 保存后的频道（有id值）
	 * */
	public Channel createChannel(Channel c) {
		return repo.save(c);
	}
	
	/*
	 * 更新频道
	 * @param c 新的频道信息，用于更新已存在的同一频道
	 * @return 更新后的频道
	 * */
	public Channel updateChannel (Channel c) {
		//TODO 仅修改用户指定的属性
		Channel saved = getChannel(c.getId());
		if (c.getTitle() != null) { 
			saved.setTitle(c.getTitle());
		}
		if (c.getQuality() != null) { 
			saved.setQuality(c.getQuality());
		}
		if (c.getUrl() != null) { 
			saved.setUrl(c.getUrl());
		}
		if (c.getComments() != null) {
			if(saved.getComments() != null) {//把新评论追加到老评论后面
				saved.getComments().addAll(c.getComments());
			}else {//用新评论代替老的空评论
				saved.setComments(c.getComments());
			}  
		}
		return repo.save(saved); //保存更新后的实体对象
	}
	/*
	 * 根据清晰度查询频道
	 * */
	public List<Channel> searchByQuality(String quality){
		return repo.findByQuality(quality);
	}
	
	/*
	 * 根据title查询频道
	 * */
	public List<Channel> searchByTitle(String title){
		return repo.findByTitle(title);
	}
	
	/*
	 * 获取冷门频道
	 * */
	public List<Channel> findColdChannels(){
		return repo.findByCommentsNull();
	}
	
	/*
	 * 分页查看
	 * */
	public List<Channel> findChannelsPage(int page){
		Page<Channel> p = repo.findAll(PageRequest.of(page,3));
		return p.toList();
	}
}
