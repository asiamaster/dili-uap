package generator;

import generator.BizNumber;

public interface BizNumberMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BizNumber record);

    int insertSelective(BizNumber record);

    BizNumber selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BizNumber record);

    int updateByPrimaryKey(BizNumber record);
}