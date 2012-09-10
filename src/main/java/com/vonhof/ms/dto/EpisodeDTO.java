package com.vonhof.ms.dto;

import com.vonhof.ms.nzb.NZBSearchResult;
import java.util.ArrayList;
import java.util.List;

public class EpisodeDTO {
    private String name;
    private int episode;
    private int season;
    private List<NZBSearchResult> nzbs = new ArrayList<NZBSearchResult>();

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NZBSearchResult> getNzbs() {
        return nzbs;
    }

    public void setNzbs(List<NZBSearchResult> nzbs) {
        this.nzbs = nzbs;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EpisodeDTO other = (EpisodeDTO) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.episode != other.episode) {
            return false;
        }
        if (this.season != other.season) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 73 * hash + this.episode;
        hash = 73 * hash + this.season;
        return hash;
    }
}
