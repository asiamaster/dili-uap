package com.dili.uap.api;

import com.dili.http.okhttp.utils.B;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.uid.domain.BizNumberRule;
import com.dili.ss.uid.service.BizNumberService;
import com.dili.ss.util.IExportThreadPoolExecutor;
import com.dili.uap.rpc.BizNumberRpc;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-07-11 16:56:50.
 */
@Api("/bizNumberApi")
@Controller
@RequestMapping("/bizNumberApi")
public class BizNumberApi {
	@Autowired
	private BizNumberService bizNumberService;
	@Autowired
	private BizNumberRpc bizNumberRpc;

	static ExecutorService executor;
	static {
		try {
			executor = ((Class<IExportThreadPoolExecutor>) B.b.g("threadPoolExecutor")).newInstance().getCustomThreadPoolExecutor();
//			executor = new ExportThreadPoolExecutor().getCustomThreadPoolExecutor();
		} catch (Exception e) {
		}
	}

	@ResponseBody
	@RequestMapping(value = "/get.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<String> get(HttpServletRequest request) {
//		System.out.println("当前端口:"+request.getLocalPort());
		BizNumberRule bizNumberRule = DTOUtils.newInstance(BizNumberRule.class);
		bizNumberRule.setDateFormat("yyyyMMdd");
		bizNumberRule.setLength(6);
		bizNumberRule.setPrefix("d");
		bizNumberRule.setName("订单编号");
		bizNumberRule.setType("order");
		bizNumberRule.setRange("1");
		return BaseOutput.success().setData(bizNumberService.getBizNumberByType(bizNumberRule));
	}

	@ResponseBody
	@RequestMapping(value = "/test2.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<String> test2() throws ExecutionException, InterruptedException {
		int COUNT = 1600;
		List<Future<String>> futures = new ArrayList<>(COUNT);
		BizNumberRule bizNumberRule = DTOUtils.newInstance(BizNumberRule.class);
		bizNumberRule.setDateFormat("yyyyMMdd");
		bizNumberRule.setLength(6);
		bizNumberRule.setPrefix("d");
		bizNumberRule.setName("订单编号");
		bizNumberRule.setType("order");
		bizNumberRule.setRange("1");
		for (int current = 0; current < COUNT; current++) {
			Future<String> future = executor.submit(() -> {
				return bizNumberService.getBizNumberByType(bizNumberRule);
			});
			futures.add(future);
		}
		List<String> list = new ArrayList<>(COUNT);
		for(int i=0; i<COUNT; i++){
			list.add(futures.get(i).get());
		}
		List<String> duplicates = listDuplicates(list);
		Collections.sort(duplicates);
		StringBuilder sb = new StringBuilder();
		int size = duplicates.size();
		for(int i=0; i<size; i++){
			sb.append(duplicates.get(i));
		}
		String result = sb.toString();
		System.out.println("重复列表:"+result);
		return BaseOutput.success().setData(result);
	}
	@ResponseBody
	@RequestMapping(value = "/test.api", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseOutput<String> test(HttpServletRequest request) throws ExecutionException, InterruptedException {
		StringBuilder sb = new StringBuilder();
		int COUNT = 9999;
//		List<String> list = new ArrayList<>(COUNT);
//		for(int i=0; i<COUNT; i++){
//			list.add(bizNumberRpc.get().getData());
//		}

		List<Future<String>> futures = new ArrayList<>(COUNT);
		for (int current = 0; current < COUNT; current++) {
			Future<String> future = executor.submit(() -> {
				return bizNumberRpc.get().getData();
			});
			futures.add(future);
		}
		List<String> list = new ArrayList<>(COUNT);
		for(int i=0; i<COUNT; i++){
			list.add(futures.get(i).get());
		}
		Collections.sort(list);
		List<String> duplicates = listDuplicates(list);
		Collections.sort(duplicates);
		int size = duplicates.size();
		for(int i=0; i<size; i++){
			sb.append(duplicates.get(i)).append(",");
		}
		String result = sb.toString();
		System.out.println("重复列表:"+result);
		return BaseOutput.success().setData(result);
	}

	/**
	 * 判断是否有重复
	 * @param list
	 * @return
	 */
	private boolean isRepleat(List list){
		return list.size() != new HashSet<String>(list).size();
	}

	/**
	 * 获取重复的列表
	 * @param list
	 * @return
	 */
	public static List listDuplicates(List list){
		List listTemp = new ArrayList();
		List duplicates = new ArrayList();
		int size = list.size();
		for(int i=0;i<size;i++){
			if(!listTemp.contains(list.get(i))){
				listTemp.add(list.get(i));
			}else{
				duplicates.add(list.get(i));
			}
		}
		return duplicates;
	}

}