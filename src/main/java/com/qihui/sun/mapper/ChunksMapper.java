package com.qihui.sun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qihui.sun.domain.ChunkEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @author bigsu
 * @description 针对表【chunks】的数据库操作Mapper
 * @createDate 2024-12-28 18:26:04
 * @Entity com.qihui.sun.domain.Chunks
 */
public interface ChunksMapper extends BaseMapper<ChunkEntity> {
    @Select("SELECT SUM(size) AS totalSize, COUNT(*) AS totalCount FROM chunks WHERE file_id = #{fileId} AND status = 'UPLOAD_SUCCESS'")
    Map<String,Long> countAndSumSizeByFileId(@Param("fileId") String fileId);

}




