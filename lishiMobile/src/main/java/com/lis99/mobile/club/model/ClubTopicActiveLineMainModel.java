package com.lis99.mobile.club.model;

import java.util.List;

public class ClubTopicActiveLineMainModel extends BaseModel {


    /**
     * activity_id : 1
     * activityimgs : [{"images":"http://i3.lis99.com/upload/club/a/1/f/a1c658aac6cac9ac5d8358453bc65fcf.jpg!middle","height":594,"width":950},{"images":"http://i3.lis99.com/upload/club/5/f/b/5fc57e782744c9755234070b57d5bffb.jpg!middle","height":768,"width":1024}]
     * title : 2016年1月20日砾石滑雪
     * activity_code : fUWeK0HZ
     * user_id : 4034
     * club_id : 190
     * club_title : 在这里测试
     * cate_id : 13
     * catename : 攀冰
     * aimaddress : 北京昌平区
     * activenum : 50
     * activitytimes : 2016-01-20 至 2016-01-21
     * tell :
     * settime : 2016-01-20 11:00:00
     * setaddress : 北京朝阳区慧忠路3号
     * gaodlongitude : 116.39242501
     * gaodelatitude : 40.01131902
     * deadline : 2016-01-20 11:00
     * harddegree : 一般
     * harddesc : 难度一般
     * consts : 1000.00
     * constdesc : 费用说明
     * leader_userid : 4034
     * leadernickname : 尘霭纷纷
     * leaderheadicon : http://i3.lis99.com/upload/photo/7/e/f/7e46add9b41eaf2082f2a569b78d47af.jpg!small
     * leaderdesc : ["活剥","可爱","女神"]
     * leadermobile : 13269888985
     * activelightspot : ["雪票、雪具、车费、住宿一价全包，什么都不用操心","专业领队免费教学，团队氛围亲切，特别适合入门新人","体验意大利专业团队设计的雪场，崇礼最具性价比"]
     * activitydetail : [{"content":"活动介绍1\n","images":"http://i3.lis99.com/upload/club/0/e/e/0eb72d329480056744914cf4cf9b11ce.jpg!middle","height":900,"width":1600},{"content":"活动介绍2","images":"http://i3.lis99.com/upload/club/2/4/3/2479e7b7211c4e860b118414e590c623.jpg!middle","height":768,"width":1024}]
     * tripdetail : [{"content":"行程详情","images":"http://i3.lis99.com/upload/club/c/d/e/cd08bb233b444816ff2ebaee3ec2693e.jpg!middle","height":787,"width":1024}]
     * equipadvise : 装备建议
     * disclaimer : 免责声明
     * catematter : 注意事项
     * reportinfo : 31
     * reportnote : ["报名须知1","报名须知2","报名须知3","报名须知4","报名须知5"]
     * createdate : 2016-01-20 10:40:24
     */

    private String activity_id;
    private String title;
    private String activity_code;
    private String user_id;
    private String club_id;
    private String club_title;
    private String cate_id;
    private String catename;
    private String aimaddress;
    private String activenum;
    private String activitytimes;
    private String tell;
    private String settime;
    private String setaddress;
    private String gaodlongitude;
    private String gaodelatitude;
    private String deadline;
    private String harddegree;
    private String harddesc;
    private String consts;
    private String constdesc;
    private String leader_userid;
    private String leadernickname;
    private String leaderheadicon;
    private String leadermobile;
    private String equipadvise;
    private String disclaimer;
    private String catematter;
    private String reportinfo;
    private String createdate;
    /**
     * images : http://i3.lis99.com/upload/club/a/1/f/a1c658aac6cac9ac5d8358453bc65fcf.jpg!middle
     * height : 594
     * width : 950
     */

    private List<ActivityimgsEntity> activityimgs;
    private List<String> leaderdesc;
    private List<String> activelightspot;
    /**
     * content : 活动介绍1

     * images : http://i3.lis99.com/upload/club/0/e/e/0eb72d329480056744914cf4cf9b11ce.jpg!middle
     * height : 900
     * width : 1600
     */

    private List<ActivitydetailEntity> activitydetail;
    /**
     * content : 行程详情
     * images : http://i3.lis99.com/upload/club/c/d/e/cd08bb233b444816ff2ebaee3ec2693e.jpg!middle
     * height : 787
     * width : 1024
     */

    private List<TripdetailEntity> tripdetail;
    private List<String> reportnote;

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setActivity_code(String activity_code) {
        this.activity_code = activity_code;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setClub_id(String club_id) {
        this.club_id = club_id;
    }

    public void setClub_title(String club_title) {
        this.club_title = club_title;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public void setCatename(String catename) {
        this.catename = catename;
    }

    public void setAimaddress(String aimaddress) {
        this.aimaddress = aimaddress;
    }

    public void setActivenum(String activenum) {
        this.activenum = activenum;
    }

    public void setActivitytimes(String activitytimes) {
        this.activitytimes = activitytimes;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }

    public void setSettime(String settime) {
        this.settime = settime;
    }

    public void setSetaddress(String setaddress) {
        this.setaddress = setaddress;
    }

    public void setGaodlongitude(String gaodlongitude) {
        this.gaodlongitude = gaodlongitude;
    }

    public void setGaodelatitude(String gaodelatitude) {
        this.gaodelatitude = gaodelatitude;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setHarddegree(String harddegree) {
        this.harddegree = harddegree;
    }

    public void setHarddesc(String harddesc) {
        this.harddesc = harddesc;
    }

    public void setConsts(String consts) {
        this.consts = consts;
    }

    public void setConstdesc(String constdesc) {
        this.constdesc = constdesc;
    }

    public void setLeader_userid(String leader_userid) {
        this.leader_userid = leader_userid;
    }

    public void setLeadernickname(String leadernickname) {
        this.leadernickname = leadernickname;
    }

    public void setLeaderheadicon(String leaderheadicon) {
        this.leaderheadicon = leaderheadicon;
    }

    public void setLeadermobile(String leadermobile) {
        this.leadermobile = leadermobile;
    }

    public void setEquipadvise(String equipadvise) {
        this.equipadvise = equipadvise;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public void setCatematter(String catematter) {
        this.catematter = catematter;
    }

    public void setReportinfo(String reportinfo) {
        this.reportinfo = reportinfo;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public void setActivityimgs(List<ActivityimgsEntity> activityimgs) {
        this.activityimgs = activityimgs;
    }

    public void setLeaderdesc(List<String> leaderdesc) {
        this.leaderdesc = leaderdesc;
    }

    public void setActivelightspot(List<String> activelightspot) {
        this.activelightspot = activelightspot;
    }

    public void setActivitydetail(List<ActivitydetailEntity> activitydetail) {
        this.activitydetail = activitydetail;
    }

    public void setTripdetail(List<TripdetailEntity> tripdetail) {
        this.tripdetail = tripdetail;
    }

    public void setReportnote(List<String> reportnote) {
        this.reportnote = reportnote;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public String getTitle() {
        return title;
    }

    public String getActivity_code() {
        return activity_code;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getClub_id() {
        return club_id;
    }

    public String getClub_title() {
        return club_title;
    }

    public String getCate_id() {
        return cate_id;
    }

    public String getCatename() {
        return catename;
    }

    public String getAimaddress() {
        return aimaddress;
    }

    public String getActivenum() {
        return activenum;
    }

    public String getActivitytimes() {
        return activitytimes;
    }

    public String getTell() {
        return tell;
    }

    public String getSettime() {
        return settime;
    }

    public String getSetaddress() {
        return setaddress;
    }

    public String getGaodlongitude() {
        return gaodlongitude;
    }

    public String getGaodelatitude() {
        return gaodelatitude;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getHarddegree() {
        return harddegree;
    }

    public String getHarddesc() {
        return harddesc;
    }

    public String getConsts() {
        return consts;
    }

    public String getConstdesc() {
        return constdesc;
    }

    public String getLeader_userid() {
        return leader_userid;
    }

    public String getLeadernickname() {
        return leadernickname;
    }

    public String getLeaderheadicon() {
        return leaderheadicon;
    }

    public String getLeadermobile() {
        return leadermobile;
    }

    public String getEquipadvise() {
        return equipadvise;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public String getCatematter() {
        return catematter;
    }

    public String getReportinfo() {
        return reportinfo;
    }

    public String getCreatedate() {
        return createdate;
    }

    public List<ActivityimgsEntity> getActivityimgs() {
        return activityimgs;
    }

    public List<String> getLeaderdesc() {
        return leaderdesc;
    }

    public List<String> getActivelightspot() {
        return activelightspot;
    }

    public List<ActivitydetailEntity> getActivitydetail() {
        return activitydetail;
    }

    public List<TripdetailEntity> getTripdetail() {
        return tripdetail;
    }

    public List<String> getReportnote() {
        return reportnote;
    }

    public static class ActivityimgsEntity {
        private String images;

        public void setImages(String images) {
            this.images = images;
        }

        public String getImages() {
            return images;
        }
    }

    public static class ActivitydetailEntity {
        private String content;
        private String images;

        public void setContent(String content) {
            this.content = content;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getContent() {
            return content;
        }

        public String getImages() {
            return images;
        }
    }

    public static class TripdetailEntity {
        private String content;
        private String images;

        public void setContent(String content) {
            this.content = content;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getContent() {
            return content;
        }

        public String getImages() {
            return images;
        }
    }
}
