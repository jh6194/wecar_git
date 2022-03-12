package com.wecar.ccontroller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.wecar.dao.CDao;
import com.wecar.dto.CDto;
import com.wecar.dto.COPDto;
import com.wecar.ucontroller.WAction;

public class WCupdate implements WAction{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		CDto cdto = new CDto();
		COPDto odto = new COPDto();
		CDao dao = new CDao();
		
		String path = request.getServletContext().getRealPath("/car/upload");
		
		try {
			MultipartRequest multi = new MultipartRequest(request, path, 1024*1024*5, "UTF-8", new DefaultFileRenamePolicy());
			
			String cno = multi.getParameter("cno");
			String cimg = multi.getFilesystemName("cimg");
			
			if (cimg == null) {
				cimg = dao.imgSelect(cno);
			}
			
			cdto.setCno(cno);
			cdto.setBrand(multi.getParameter("brand"));
			cdto.setModel(multi.getParameter("model"));
			cdto.setType(multi.getParameter("type"));
			cdto.setPersonnel(multi.getParameter("personnel"));
			cdto.setVehicle_year(multi.getParameter("vehicle_year"));
			cdto.setFuel(multi.getParameter("fuel"));
			cdto.setLno(multi.getParameter("lno"));
			cdto.setCprice(Integer.parseInt(multi.getParameter("cprice")));
			cdto.setCimg(cimg);
			
			odto.setCno(cno);
			String[] checkbox = multi.getParameterValues("option");
			
			if (checkbox != null) {
				for (int i = 0; i < checkbox.length; i++) {
					switch(Integer.parseInt(checkbox[i])) {
					case 1: odto.setSmoking(true);		break; 
					case 2: odto.setNavigation(true);	break; 
					case 3: odto.setSmart(true); 		break; 
					case 4: odto.setRear(true);		 	break; 
					case 5: odto.setBluetooth(true); 	break; 
					}
				}
			}

			if (dao.carUpdate(cdto) < 1 || dao.optionUpdate(odto) < 1) {
				out.print("<script>alert('수정 실패'); location.href = 'detail.car?cno="+cno+"'</script>");
			} else {
				out.print("<script>alert('수정 성공'); location.href = 'detail.car?cno="+cno+"'</script>");
			}
		} catch (Exception e) { e.printStackTrace(); }
	}

}
