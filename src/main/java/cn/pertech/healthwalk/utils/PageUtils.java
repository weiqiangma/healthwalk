package cn.pertech.healthwalk.utils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.LinkedList;
import java.util.List;

public class PageUtils {
    /**
     * Discreption: 手动对list分页
     * @param pageNum
     * @param pageSize
     * @param list
     * @param <T>
     * @return
     */
        public static <T> PageInfo<T> list2PageInfo(Integer pageNum, Integer pageSize, List<T> list) {
            //实现list分页
            PageHelper.startPage(pageNum, pageSize);
            int pageStart = pageNum == 1 ? 0 : (pageNum - 1) * pageSize;
            int pageEnd = list.size() < pageSize * pageNum ? list.size() : pageSize * pageNum;
            List<T> pageResult = new LinkedList<T>();
            if (list.size() > pageStart) {
                pageResult = list.subList(pageStart, pageEnd);
            }
            PageInfo<T> pageInfo = new PageInfo<T>(pageResult);
            //获取PageInfo其他参数
            pageInfo.setTotal(list.size());
            int endRow = pageInfo.getEndRow() == 0 ? 0 : (pageNum - 1) * pageSize + pageInfo.getEndRow() + 1;
            pageInfo.setEndRow(endRow);
            boolean hasNextPage = list.size() <= pageSize * pageNum ? false : true;
            pageInfo.setHasNextPage(hasNextPage);
            boolean hasPreviousPage = pageNum == 1 ? false : true;
            pageInfo.setHasPreviousPage(hasPreviousPage);
            pageInfo.setIsFirstPage(!hasPreviousPage);
            boolean isLastPage = (list.size() > pageSize * (pageNum - 1) && list.size() <= pageSize * pageNum) ? true : false;
            pageInfo.setIsLastPage(isLastPage);
            int pages = list.size() % pageSize == 0 ? list.size() / pageSize : (list.size() / pageSize) + 1;
            pageInfo.setNavigateLastPage(pages);
            int[] navigatePageNums = new int[pages];
            for (int i = 1; i < pages; i++) {
                navigatePageNums[i - 1] = i;
            }
            pageInfo.setNavigatepageNums(navigatePageNums);
            int nextPage = pageNum < pages ? pageNum + 1 : 0;
            pageInfo.setNextPage(nextPage);
            pageInfo.setPageNum(pageNum);
            pageInfo.setPageSize(pageSize);
            pageInfo.setPages(pages);
            pageInfo.setPrePage(pageNum - 1);
            pageInfo.setSize(pageInfo.getList().size());
            int starRow = list.size() < pageSize * pageNum ? 1 + pageSize * (pageNum - 1) : 0;
            pageInfo.setStartRow(starRow);
            return pageInfo;
        }

}
