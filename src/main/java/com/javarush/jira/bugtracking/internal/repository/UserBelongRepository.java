package com.javarush.jira.bugtracking.internal.repository;

import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.common.BaseRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface UserBelongRepository extends BaseRepository<UserBelong> {

    public UserBelong findUserBelongByObjectId(Long id);
    public int deleteByObjectId(Long id);
}
