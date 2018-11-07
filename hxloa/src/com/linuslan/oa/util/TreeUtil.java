package com.linuslan.oa.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.linuslan.oa.common.TreeModel;

public class TreeUtil {
	
	public static List<? extends TreeModel> buildTree(List<? extends TreeModel> list) throws RuntimeException {
		List<TreeModel> newTrees = new ArrayList<TreeModel> ();
		try {
			Map<Long, ? extends TreeModel> map = TreeUtil.parseMapById(list);
			Iterator<? extends TreeModel> iter = list.iterator();
			TreeModel tree = null;
			while(iter.hasNext()) {
				tree = iter.next();
				if(null == tree.getPid()) {
					newTrees.add(tree);
				} else {
					TreeModel parent = map.get(tree.getPid());
					if(null != parent) {
						parent.getChildren().add(tree);
						TreeUtil.sort(parent.getChildren());
					} else {
						newTrees.add(tree);
					}
				}
			}
			TreeUtil.sort(newTrees);
		} catch(Exception ex) {
			ex.printStackTrace();
			CodeUtil.throwRuntimeExcep("生成树异常，异常原因："+CodeUtil.getStackTrace(ex));
		}
		
		return newTrees;
	}
	
	/**
	 * 用id作为key存放在map中
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static Map<Long, ? extends TreeModel> parseMapById(List<? extends TreeModel> list) throws Exception {
		Map<Long, TreeModel> treeMap = new HashMap<Long, TreeModel> ();
		if(null != list && 0 < list.size()) {
			Iterator<? extends TreeModel> iter = list.iterator();
			TreeModel tree = null;
			while(iter.hasNext()) {
				tree = iter.next();
				treeMap.put(tree.getId(), tree);
			}
		}
		return treeMap;
	}
	
	private static void sort(List<TreeModel> list) {
		if(null != list && 0 < list.size()) {
			for(int i = 0; i < list.size() - 1; i ++) {
				for(int j = 0; j < (list.size() - 1) - i; j ++) {
					TreeModel pre = list.get(j);
					TreeModel next = list.get(j+1);
					TreeModel temp = null;
					if(null != pre) {
						if(null != next) {
							if(null == pre.getOrderNo()) {
								pre.setOrderNo(0);
							}
							if(null == next.getOrderNo()) {
								next.setOrderNo(0);
							}
							if(pre.getOrderNo() > next.getOrderNo()) {
								temp = pre;
								pre = next;
								next = temp;
							} else if(pre.getOrderNo() == next.getOrderNo()) {
								if(pre.getId() > next.getId()) {
									temp = pre;
									pre = next;
									next = temp;
								}
							}
						}
					} else {
						if(null != next) {
							temp = pre;
							pre = next;
							next = temp;
						}
					}
					list.set(j, pre);
					list.set(j+1, next);
				}
			}
		}
	}
	
}
