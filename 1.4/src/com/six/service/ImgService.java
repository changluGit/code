package com.six.service;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.struts2.ServletActionContext;

import com.six.action.BaseAction;
import com.six.entity.Store;

public class ImgService extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 缩放图像（按高度和宽度缩放）
	 * 
	 * @param srcImageFile
	 *            源图像文件地址
	 * @param result
	 *            缩放后的图像地址
	 * @param height
	 *            缩放后的高度
	 * @param width
	 *            缩放后的宽度
	 */
	public final static void scale(String srcImageFile, String result,
			int height, int width) {
		try {
			double ratio = 0.0; // 缩放比例
			File f = new File(srcImageFile);
			BufferedImage bi = ImageIO.read(f);
			Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
			// 计算比例
			if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
				if (bi.getHeight() > bi.getWidth()) {
					ratio = (new Integer(height)).doubleValue()
							/ bi.getHeight();
				} else {
					ratio = (new Integer(width)).doubleValue() / bi.getWidth();
				}
				AffineTransformOp op = new AffineTransformOp(
						AffineTransform.getScaleInstance(ratio, ratio), null);
				itemp = op.filter(bi, null);
			}
			ImageIO.write((BufferedImage) itemp, "JPEG", new File(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 如果文件夹不存在，创建文件夹
	 * 
	 */
	public static void createDir(){
		String path = ServletActionContext.getServletContext().getRealPath("/")+"\\uploadImg";
		File folder = new File(path);
		if(!folder.exists())
		{
			folder.mkdir();
		}
	}

	/**
	 * 图片上传处理
	 * 
	 * @param file页面上传的文件list store
	 * @return pictureAddress 图片地址
	 * @throws Exception 
	 */
	public static String uploadImg(Store store,List<File> file) throws Exception {
		createDir();
		String imgPagePath = "uploadImg/";
		InputStream is = new FileInputStream(file.get(0));

		String path = ServletActionContext.getServletContext().getRealPath("/");
		System.out.println(path);
		String pictureName = getSysTime() + store.getId() + ".jpg";
		String pictureAddress = imgPagePath + pictureName;
		File destFile = new File(path + imgPagePath, pictureName);

		OutputStream os = new FileOutputStream(destFile);

		byte[] buffer = new byte[400];

		int length = 0;

		while ((length = is.read(buffer)) > 0) {
			os.write(buffer, 0, length);
		}
		is.close();
		os.close();

		String srcImgpath = path + pictureAddress;
		scale(srcImgpath, srcImgpath, 150, 150);
		return pictureAddress;
	}

	// 得到系统时间字符串
	public static String getSysTime() {
		Date date = new Date();
		String temp = date.toString().replaceAll(" ", "");
		temp = temp.replaceAll(":", "");
		System.out.println(temp);
		return temp;
	}

	@Override
	public Object getModel() {
		return null;
	}

}
