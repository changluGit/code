package com.six.action;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.six.entity.PetCard;
import com.six.entity.User;
import com.six.service.CardManageService;

@Component
public class CardManageAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private CardManageService cardManageService;
	private User user;

	public CardManageService getCardManageService() {
		return cardManageService;
	}

	@Resource
	public void setCardManageService(CardManageService cardManageService) {
		this.cardManageService = cardManageService;
	}

	/**
	 * 通过user找到对应的储值卡,通过card_id找到对应的消费记录
	 * 
	 * @return
	 */
	public String getPetCardAndConm() {
		user = (User) getRequest().getSession().getAttribute("loginedUser");
		PetCard petCard = cardManageService.findPetCard(user);
		List<?> consumptionList = cardManageService.getConmptionList(petCard);
		getRequest().setAttribute("petCard", petCard);
		getRequest().setAttribute("consumptionList", consumptionList);
		return SUCCESS;
	}

	/**
	 * 充值
	 * 
	 * @return
	 */
	public String recharge() {
		user = (User) getRequest().getSession().getAttribute("loginedUser");
		int rechargeSum = 0;
		String money = getRequest().getParameter("rechargeSum");
		if (!"".equals(money) && money != null && money.indexOf('.') == -1 && money.length()<10) {
			System.out.println("enter recharge()22222222222222222");
			rechargeSum = Integer.parseInt(money);
			cardManageService.recharge(user, rechargeSum);	
			getRequest().setAttribute("rechargeSum", rechargeSum);
		}
		
		getPetCardAndConm();
		return SUCCESS;
	}

	/**
	 * 扣钱
	 * 
	 * @return
	 */
	public String deduct() {
		user = (User) getRequest().getSession().getAttribute("loginedUser");
		int deductSum = 0;
		if (!"".equals(getRequest().getParameter("deductSum"))
				&& getRequest().getParameter("rechargeSum") != null) {
			deductSum = Integer.parseInt(getRequest().getParameter(
					"rechargeSum"));
			cardManageService.deduct(user, deductSum);
		}	
		getPetCardAndConm();
		return SUCCESS;
	}
	
	public void createCard(){
		user = (User) getRequest().getSession().getAttribute("loginedUser");
		System.out.println("user:" + user.getName());
		cardManageService.createPetCard(user);
	}
	

	@Override
	public Object getModel() {

		return null;
	}

}
