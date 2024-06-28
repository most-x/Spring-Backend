package kr.co.mostx.japi.repository;

import kr.co.mostx.japi.entity.Servey;

import java.util.List;

public interface ServeyCustomRepository {
    List<Servey> searchServey(String serveyNumber);
}

