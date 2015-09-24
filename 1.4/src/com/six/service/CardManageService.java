package com.six.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.six.dao.BaseDao;
import com.six.entity.Businessman;
import com.six.entity.ConsumptionRecord;
import com.six.entity.PetCard;
import com.six.entity.User;

@Component
public class CardManageService {

	private BaseDao baseDao;
	private PetCard petCard;

	public BaseDao getBaseDao() {
		return baseDao;
	}

	@Resource
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	/**
	 * 为用户初始化储值卡账户,state=1表示被冻结，为0表示正常使用
	 * 
	 * @Param user
	 * @return Petcard成功返回储值卡对象，失败返回null
	 */
	public boolean createPetCard(User user){
		
		if(null != user){
			PetCard petCard = new PetCard();
			String cardNumber = "";
			//随机生成12位数字，加上前6222前4位数字作为账号
			StringBuffer sb = new StringBuffer();
			sb.append("6222");
			for(int i=0;i<12;i++){
				int temp = (int) (Math.random()*10);
				sb.append(temp);
			}
			cardNumber = sb.toString();
			System.out.println(cardNumber);
			petCard.setBalance(0.0);
			petCard.setCardNumber(cardNumber);
			petCard.setUser(user);
			petCard.setCreateCardTime(getSystime());
			petCard.setState("0");
			baseDao.save(petCard);
			user.setPetCard(petCard);
			baseDao.saveOrUpdate(user);
			return true;
			
		}else{
			return false;
		}
		
	}


	/**
	 * 通过user找到对应的储值卡
	 * 
	 * @param user
	 * @return PetCard
	 */
	public PetCard findPetCard(User user) {
		petCard = (PetCard) baseDao.findByPropertyUnique(PetCard.class, "user", user);
		return petCard;
	}

	/**
	 * 通过card_id找到对应的消费记录
	 * 
	 * @param petCard
	 * @return cardList
	 */
	public List<?> getConmptionList(PetCard petCard) {
		List<?> cardList = baseDao.findByProperty(ConsumptionRecord.class, "petCard", petCard);
		return cardList;
	}

	/**
	 * 为用户充值
	 * 
	 * @param user用户对象
	 * @param rechargeSum充值金额
	 * @return
	 */
	public void recharge(User user, int rechargeSum) {
		petCard = (PetCard) baseDao.findByPropertyUnique(PetCard.class, "user", user);
		if (null != petCard) {
			double balance = petCard.getBalance() + rechargeSum;
			petCard.setBalance(balance);
			baseDao.update(petCard);
			ConsumptionRecord consumption = new ConsumptionRecord();
			consumption.setPetCard(petCard);
			consumption.setConsumptionAmount(rechargeSum);
			consumption.setOccurrenceTime(getSystime());
			baseDao.save(consumption);
		}
	}

	/**
	 * 为商家充值
	 * 
	 * @param businessman用户对象
	 * @param rechargeSum充值金额
	 * @return
	 */
	public void recharge(Businessman businessman, int rechargeSum) {
		petCard = (PetCard) baseDao.findByPropertyUnique(PetCard.class, "businessman", businessman);
		if (null != petCard) {
			double balance = petCard.getBalance() + rechargeSum;
			petCard.setBalance(balance);
			baseDao.update(petCard);
			ConsumptionRecord consumption = new ConsumptionRecord();
			consumption.setPetCard(petCard);
			consumption.setConsumptionAmount(rechargeSum);
			consumption.setOccurrenceTime(getSystime());
			baseDao.save(consumption);
		}
	}

	/**
	 * 扣钱
	 * 
	 * @param user用户对象
	 * @param deductSum扣除金额
	 * @return 余额不足，返回false,否则，返回true
	 */
	public boolean deduct(User user, int deductSum) {
		petCard = (PetCard) baseDao.findByPropertyUnique(PetCard.class, "user", user);
		if (null != petCard && petCard.getBalance() >= deductSum) {
			double balance = petCard.getBalance() - deductSum;
			petCard.setBalance(balance);
			baseDao.update(petCard);
			ConsumptionRecord consumption = new ConsumptionRecord();
			consumption.setPetCard(petCard);
			consumption.setConsumptionAmount(-deductSum);
			consumption.setOccurrenceTime(getSystime());
			baseDao.save(consumption);
			return true;
		} else {
			return false;
		}

	}

	
	/**
	 * 扣钱
	 * 
	 * @param business商家对象
	 * @param deductSum扣除金额
	 * @return 余额不足，返回false,否则，返回true
	 */
	public boolean deduct(Businessman businessman, int deductSum) {
		petCard = (PetCard) baseDao.findByPropertyUnique(PetCard.class, "businessman", businessman);
		if (null != petCard && petCard.getBalance() >= deductSum) {
			double balance = petCard.getBalance() - deductSum;
			petCard.setBalance(balance);
			baseDao.update(petCard);
			ConsumptionRecord consumption = new ConsumptionRecord();
			consumption.setPetCard(petCard);
			consumption.setConsumptionAmount(-deductSum);
			consumption.setOccurrenceTime(getSystime());
			baseDao.save(consumption);
			return true;
		} else {
			return false;
		}

	}
	
	/**
	 * 得到系统时间
	 * 
	 * @return Timestamp ts
	 */
	public Timestamp getSystime(){
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		return ts;
	}
	
	
}
