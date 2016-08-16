package com.lis99.mobile.club.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ClubTopicActiveSeriesLineMainModel extends BaseModel implements ShareInterface{




    @SerializedName("activity_id")
    public int activityId;
    @SerializedName("activity_code")
    public String activityCode;
    @SerializedName("title")
    public String title;
    @SerializedName("user_id")
    public int userId;
    @SerializedName("club_id")
    public int clubId;
    @SerializedName("club_title")
    public String clubTitle;
    @SerializedName("club_iconv")
    public String clubIconv;
    @SerializedName("cate_id")
    public String cateId;
    @SerializedName("catename")
    public String catename;
    @SerializedName("batch_desc")
    public String batchDesc;
    @SerializedName("activitytimes")
    public String activitytimes;
    @SerializedName("consts")
    public String consts;
    @SerializedName("batch_total")
    public int batchTotal;
    @SerializedName("settime")
    public String settime;
    @SerializedName("aimaddress")
    public String aimaddress;
    @SerializedName("setaddress")
    public String setaddress;
    @SerializedName("gaodlongitude")
    public String gaodlongitude;
    @SerializedName("gaodelatitude")
    public String gaodelatitude;
    @SerializedName("harddegree")
    public String harddegree;
    @SerializedName("harddesc")
    public String harddesc;
    @SerializedName("constdesc")
    public String constdesc;
    @SerializedName("leader_userid")
    public int leaderUserid;
    @SerializedName("leadernickname")
    public String leadernickname;
    @SerializedName("leaderheadicon")
    public String leaderheadicon;
    @SerializedName("leadernote")
    public String leadernote;
    @SerializedName("leadermobile")
    public String leadermobile;
    @SerializedName("equipadvise")
    public String equipadvise;
    @SerializedName("disclaimer")
    public String disclaimer;
    @SerializedName("catematter")
    public String catematter;
    @SerializedName("reportinfo")
    public int reportinfo;
    @SerializedName("createdate")
    public String createdate;
    @SerializedName("is_jion")
    public int isJion;
    @SerializedName("shareTxt")
    public String shareTxt;


    @SerializedName("aimid")
    public String aimid;
    @SerializedName("aimlongitude")
    public String aimlongitude;
    @SerializedName("aimlatitude")
    public String aimlatitude;


    @SerializedName("commentlist")
    public ArrayList<CommentEntity> commentlist;

    public ArrayList<TagEntity> taglist;
    /**
     * images : http://i3.lis99.com/upload/club/c/1/5/c140e540a4949b2ecde2f7d7b8f0e265.png
     * width : 720
     * height : 453
     */

    @SerializedName("activityimgs")
    public ArrayList<ActivityimgsEntity> activityimgs;
    @SerializedName("leaderdesc")
    public ArrayList<String> leaderdesc;
    @SerializedName("activelightspot")
    public ArrayList<String> activelightspot;
    /**
     * content : 雅安碧峰峡因林木葱茏、四季青碧而得名。传说是补天英雄女娲所化而成，景区内60
     * 多个景点均与女娲有关，颇为神秘。在碧峰峡风景区你能呼吸到群山幽谷蕴酿的芳醇空气，寻找到万古犹存的补天遗迹，以及那些曾在此发生过的爱的、美的传说故事，它象一首空灵的朦胧诗，一幅淡雅的水墨画，等待你去品味，去赏析。
     * images : http://i3.lis99.com/upload/club/1/0/6/104c1b0def86a97d42b6a864cbf29d56.png
     * height : 429
     * width : 721
     */

    @SerializedName("activitydetail")
    public ArrayList<ActivitydetailEntity> activitydetail;
    /**
     * content : D0：成都集合
     本次行程可选择跟烟台前往的自驾越野车，或是根据个人时间选择航班飞往成都集合，再乘坐成都当地越野车。乘坐航班前往的亲们，前线户外可为您代订往返机票事宜，具体详情可致电前线户外商榷。
     乘坐自驾车前往成都的亲们，请尽早联系前线户外确定位置。我们将提前出发，于当日抵达成都休整，采买，品尝成都美食。等待全体成员齐整，一起开始神奇美好的旅程。

     D1：成都—碧峰峡—雅安 住雅安
     早餐后从成都出发，高速约一个半小时抵达雅安。雅安市地处川西南，以生产蒙顶茶闻名，中国西南地区著名的茶马古道就在这里。雅安地处四川盆地和青藏高原的过渡地带，地理形态独特，汉藏文化交汇融合，文化历史底蕴丰厚，山川秀美，生态良好，是天然氧吧。造就了雅安独特的三大特色“景观”：“雅雨”、“雅鱼”、“雅女”，并称雅安三雅。我们将直接驱车前往碧峰峡景区游览。
     雅安碧峰峡因林木葱茏、四季青碧而得名。传说是补天英雄女娲所化而成，景区内60
     多个景点均与女娲有关，颇为神秘。在碧峰峡风景区你能呼吸到群山幽谷蕴酿的芳醇空气，寻找到万古犹存的补天遗迹，以及那些曾在此发生过的爱的、美的传说故事，它象一首空灵的朦胧诗，一幅淡雅的水墨画，等待你去品味，去赏析。 碧峰峡风景区由两条峡谷构成，左峡谷长7公里，右峡谷长6公里，呈V字型，是一个封闭式的可循环游览景区。峡宽30-70米，海拔700-1971米，峡壁高度100-200米，青峰对峙，景色秀雅，是休闲度假，避暑纳凉的绝佳之地。碧峰峡景区包含黄龙峡、天仙桥、天然盆景、千层岩瀑布、白龙潭瀑布、女娲池、滴水栈道等众多景点。游览完后，乘车前往雅安县城指定酒店休息。

     D2：雅安—海螺沟 住海螺沟
     早餐后，经石棉前往海螺沟，午餐后进入海螺沟景区游览，海螺沟位于四川省泸定县磨西镇，贡嘎山东坡，是青藏高原东缘的极高山地。海螺沟位于贡嘎雪峰脚下，以低海拔现代冰川著称于世。晶莹的现代冰川从高峻的山谷铺泻而下；巨大的冰洞、险峻的冰桥，使人如入神话中的水晶宫。特别是举世无双的大冰瀑布，高达1000多米，宽约1100米，比著名的黄果树瀑布大出十余倍，瑰丽非凡。
     海螺沟是亚洲最东低海拔现代冰川发现地，海拔2850米。其大冰瀑布高1080米，宽0
     .5米－1100米，是中国至今发现的最高大冰瀑布。沟内蕴藏有大流量沸热温冷矿泉，大面积原始森林和高的冰蚀山峰，大量的珍稀动植物资源，金、银山交相辉映，蔚为壮观。
     游览完毕后，入住海螺沟当地温泉酒店。

     D3：海螺沟--木格措---康定 住康定
     早餐后，驱车离开海螺沟，经康定前往木格措景区，午餐后进入木格措景区游览，木格措坐落于贡嘎山脉中段，距离康定市区约17公里，系国家AAAA
     级旅游景区、世界自然遗产提名地、国家级重点风景名胜区。景区面积约350
     平方公里，由杜鹃峡、芳草坪、七色海、药池沸泉、木格措（野人海）和红海草原六个景点组成，附近有其他多个高山湖泊和温泉，原始森林、草原和雪山景观互相交融。景区以高原湖泊、原始森林、温泉、雪峰、奇山异石及长达8公里的千瀑峡，构成了秀丽多彩的景观。景点配置巧夺天工，是一处游览、娱乐、观赏、休息、疗养、健身、避暑、科考的理想胜地。
     杜鹃峡是横系木格措景区的一条绚丽腰带。杜鹃峡东连七色海，西接野人海，峡谷长8
     公里。峡谷中，溪涧时而奔流，时而舒缓，飞珠溅玉，溪岸两边林木葱郁，尤以原始杜鹃林引人注目。这里的杜鹃花千姿百态，艳丽多彩。杜鹃峡将温泉、湖泊、草坪、奇峰异石和激流飞瀑紧系在一起，峡谷内呈现出浓郁的原始自然风光。行走其间，令人心旷神怡。我们将带领大家前往木格措去探寻漫山的野坡杜鹃，去看真正名副其实的木格措（野人海）以及红海草原，登高遥望，感受沧桑的茶马古道遗迹。
     游览完后，驱车前往康定指定酒店休息。

     D4：康定---泉华滩---子梅垭口---上木居 住上木居
     早餐后，从康定出发，一路美景，前往贡嘎雪山脚下的木居村，当天拍摄元素非常多！但毫无疑问，泉华滩和子梅垭口才是今天的主题，中午时分抵达上木居，安排上木居藏家入住，然后统一集合乘车前往玉龙西村泉华滩摄影创作。泉华滩位于六巴乡玉龙西村，长达900多米，宽100余米，依山而下形成8个泉华阶地。每个阶地上有十几个大小不同、形态各异的五色彩池，水深40-70厘米，彩池里水草相依，石花点点，疑为瑶池降临人间，这里也是贡嘎主峰的最佳摄影点之一。
     泉华滩游览时间约三小时，泉华滩大的台阶有三级，一般我们只参观最下面一级看看钙化池，徒步半小时即到；徒步到第二级可以看到贡嘎雪山的头顶，但这要多徒步四十分钟才能上去；徒步到第三级可以看到一米高的天然喷泉和贡嘎山的大半个脸，但这里海拔四千米以上，需要付出的体力更多。16:00离开泉华滩，乘车一路惊叹一路攀升前往子梅垭口，这里可近距离的观赏贡嘎雪山，并在子梅垭口拍摄贡嘎夕照（此行另一亮点）。拍摄完后下山回到上木居住地，在藏家享受藏式特色晚餐。

     D5：上木居---塔公---八美---道孚---炉霍 住炉霍
     早餐后，离开上木居，前往塔公八美。塔公在藏语意为“菩萨喜欢的地方”。塔公寺是藏传佛教萨迦派著名寺庙之一，有“小大昭寺”之称，是康巴地区藏民朝圣地之一。每年寺庙都要举行一次盛大的佛事和跳神活动，寺壁挂满唐卡藏画，殿内生动地显示出寺内僧侣及民间艺人的高超技艺。藏式浮雕、彩塑、壁画及酥油制品以及造型各异的浮屠古塔林，具有十分独特的民族宗教文化特色。
     沿途的塔公草原距康定城110
     公里，自康定沿川藏线西行，翻越折多山，过新都桥后北行达塔公寺。沿线的河流、草原、森林、山体、寺庙、藏房建筑和浓郁的藏乡风情构成该景区。塔公草原地势和缓、水草丰茂、物产甚丰，既产虫草、松茸、牙獐，又有酪、奶油、人参果。
     八美镇很小，但是是重要的往来交通中转站。在这里我们将看到美丽的雅拉雪山。八美的雅拉雪山，是惠远寺和周边藏民顶礼膜拜的神山。藏传古籍《神山志易入解脱之道》中对该山的记载，称其为“第二香巴拉”。 从西面看雅拉如同燃烧的大火，在北方看如同贤淑的八美女子。
     道孚位于青藏高原丘状高原向山地地貌的过渡带上，因地貌复杂，造成这里景观多样，雪峰、峡谷、江河、湖泊、瀑布、土石林等一应俱全，是一个很漂亮的地方。道孚民居融民族风情、建筑、绘画、雕刻艺术为一体，非常有特色，堪称藏区一绝康巴一绝。
     道孚位于303省道新都桥至炉霍的中间位置，南距八美76公里，北距炉霍71公里。地处鲜水河畔的一个谷地之中，海拔比周围的塔公、八美、道孚都要低一些，不到3000
     米，而前面提到的周围几个地方海拔都在3000米以上，因此，这里的气候、植被都比外面要好些。
     傍晚时分，抵达炉霍县城，入住指定酒店休息。

     D6：炉霍---五明佛学院---色达 住色达
     早餐后从炉霍出发，我们将会在上午抵达喇荣五明佛学院，留给大家充分的时间停留在宛若尘世外的佛教殿堂，大家可以前往佛学院大殿听一场讲经，可四处走走或拍摄或与修行者访谈一番，更有感兴趣天葬的朋友，前往天葬台观看天葬仪式。也可以先去观看佛学院全景。你定将无比震撼。色达佛学院是世界上最大的藏传佛学之一。 这里的僧舍很壮观，连绵数公里的山谷布满了密密麻麻的小木棚屋。谷底和山梁上分布着几座寺庙和佛堂，建筑规模虽都不很大，但装饰考究而辉煌；身披桨红色僧袍的喇嘛和尼姑来来往往，空气中充满生机和祥和气氛。
     在佛学院最高的山峰上，有一个金碧辉煌的“坛城”。它的上半部分是转经的地方，据说如果你有什么疾病，在这里转上一百圈就能够好；下面一层是转经筒，金色的圆筒在人们干枯的手转过之后留下一串悠长的嘎吱嘎吱的响声。夜幕降临时，更可看到坛城夜景。
     游览结束后，我们将带着各自不同的感悟和心绪入住色达休息。

     D7：色达---丹巴---中路藏寨 住中路藏寨
     早餐后从色达出发，经八美前往丹巴，下午抵达中路藏寨，前往中路观景台，中路比甲居更是惊艳，大家可以尽情拍摄，尽情享受眼前美景。中路藏寨是丹巴藏寨的重要组成部分之一。中路现有88
     座巍峨的古雕，高从30－60
     米不等，历经千年巍然耸立。中路的藏房是几乎是藏区民居中最高的，一般分为五层，一层为畜圈；二层为锅庄（厨房、会客、聚会等）；三层为客房及卧室；四层为敞房（兼具晾晒粮食、经堂之用；五层为卓日（敬四方神、煨桑外）。这些美丽的藏房错落有致地分布在田野中，组成一道道美丽的风景线。每逢深秋和花开时节，中路乡是摄影发烧友最多的地方。我们会带着大家一路沿着步游道，上到最高的观景台-柯热豁古东女国遗址，之后大家就可以自由活动和拍摄了。中路藏寨海拔2200米。

     D8：中路藏寨---日隆---长坪沟 住日隆
     早上继续晨拍，到中路日出观景台疯狂拍摄后早餐，早餐后，9点出发，驱车前往日隆四姑娘山长坪沟，车程约2小时，抵达四姑娘山游览长坪沟，去感觉穿越原始森林、穿过小溪、遥望雪山，在此可以看到大姑娘、二姑娘、三姑娘及四姑娘山，另可观赏到樟木藏寨、格鲁派喇嘛寺、古柏幽道、枯树滩、净心洞、洗身池、擂鼓石、金鸡岭等。长坪沟位于四姑娘山山脚下，全长30余公里。沟内植被丰富茂盛，分布着大片的原始森林，古柏苍松交错攀附，原始古朴。林深尽处，大片高山草甸置于群山环绕之中，给人远离尘世的感悟。没有人工修饰，一切自然天成，红石阵那激情的颜色，张扬着大自然的伟力。进入沟内根据自身情况选择骑马或徒步游览。游览结束后，入住日隆镇指定酒店休息。

     D9：日隆---夹金山---雅安---成都 住成都
     早餐后，离开日隆，翻越夹金山，经雅安回到成都。夹金山森林公园水源丰富，构成了她独具特色的景致。这里溪流纵横，在群山身上也是溪流长挂，有如丝丝银线，山之动脉。如此丰富的水源，汇成了罗棋布的海子群和形态各异的大面积冰川。最大的夹金海子水域面积16万平方米，碧波荡漾，水深难测，传说是山神坐骑——犀牛吐水而成。从高处俯瞰，海子如镶在夹金山里的一颗硕大的碧绿宝石。秋季层林尽染，红、黄、绿叶垒在一起，如梦似幻，让人目不暇接。有如此广袤的原始森林，有如此的清新温润的空气，游客到此会忘掉自己身处何地，流连忘返。夹金山又是中国工农红军万里长征徒步翻越的第一座大雪山，还是当年中国工农红军一方面军万里长征与红四方面胜利会师的地方。因此夹金山被载入中国革命历史的光荣史。傍晚抵达成都入住成都指定酒店。

     D10：成都---烟台
     结束行程，乘坐航班回烟的队友，送往机场，根据您选乘的航班当日回到温暖的家。时间充裕跟随自驾车回烟的队友，将一路欢歌笑语隔日回到温暖的家。
     * images :
     * height : 0
     * width : 0
     */

    @SerializedName("tripdetail")
    public ArrayList<TripdetailEntity> tripdetail;
    @SerializedName("reportnote")
    public ArrayList<String> reportnote;

    //  装备列表
    public ArrayList<EquipRecommend> zhuangbeilist;


    static public class EquipRecommend implements EquipRecommendInterFace, Serializable
    {


        public String zhuangbei_id;

        public String zhuangbei_image;

        public String zhuangbei_title;

        public int zhuangbei_star;


        @Override
        public String getImgUrl() {
            return zhuangbei_image;
        }

        @Override
        public String getTitle() {
            return zhuangbei_title;
        }

        @Override
        public int getStar() {
            return zhuangbei_star;
        }

        @Override
        public String getPrice() {
            return null;
        }

        @Override
        public String getId() {
            return zhuangbei_id;
        }

    }

    public static class ActivityimgsEntity implements Serializable{
        @SerializedName("images")
        public String images;
        @SerializedName("width")
        public int width;
        @SerializedName("height")
        public int height;
    }

    public static class ActivitydetailEntity implements Serializable{
        @SerializedName("content")
        public String content;
        @SerializedName("images")
        public String images;
        @SerializedName("height")
        public int height;
        @SerializedName("width")
        public int width;
    }

    public static class TripdetailEntity implements Serializable{
        @SerializedName("content")
        public String content;
        @SerializedName("images")
        public String images;
        @SerializedName("height")
        public int height;
        @SerializedName("width")
        public int width;
    }


    public static class TagEntity implements  Serializable{
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;


    }

    public static class CommentEntity implements Serializable{
        @SerializedName("user_id")
        public String user_id;

        @SerializedName("nickname")
        public String nickname;

        @SerializedName("content")
        public String content;

        @SerializedName("star")
        public String star;

        @SerializedName("createtime")
        public String createtime;

       /* @SerializedName("image")
        public String image;
*/
        @SerializedName("usercatelist")
        public ArrayList<String> usercatelist;
    }


    /**
     * 是否是置顶 2置顶， 其他 解除置顶
     */
    @Override
    public String getStick() {
        return "";
    }

    /**
     * 是否是置顶 2置顶， 其他 解除置顶
     *
     * @param s
     */
    @Override
    public void setStick(String s) {

    }

    /**
     * 是否显示报名管理， 1线下活动， 2 线上活动，999 系列活动 ， 显示管理， 其他不显示
     */
    @Override
    public String getCategory() {
        return "999";
    }

    /**
     * 创始人标记， 是否显示删除 1 创始人（显示删除）
     */
    @Override
    public String getIsJoin() {
        return ""+isJion;
    }

    /**
     * 是否为新版的线下活动贴, null 否， 不为空 是新版活动帖
     *
     * @return
     */
    @Override
    public String getNewActive() {
        return activityCode;
    }

    /**
     * 俱乐部Id
     *
     * @return
     */
    @Override
    public String getClubId() {
        return ""+clubId;
    }

    /**
     * 帖子Id
     *
     * @return
     */
    @Override
    public String getTopicId() {
        return ""+activityId;
    }

    /**
     * 图片Url 地址
     *
     * @return
     */
    @Override
    public String getImageUrl() {
        String imgUrl = "";

        if (activityimgs == null || activityimgs.size() == 0) {

        } else {
            imgUrl = activityimgs.get(0).images;
        }

        return imgUrl;
    }

    /**
     * 分享显示标题
     *
     * @return
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * 分享内容
     *
     * @return
     */
    @Override
    public String getShareTxt() {
        return shareTxt;
    }

    /**
     * 分享连接地址
     *
     * @return
     */
    @Override
    public String getShareUrl() {
        return ("http://m.lis99.com/club/activity/detail/"+activityCode);
    }

    /**
     * 帖子类型
     *
     * @return 0, 其他， 1， 线上
     */
    @Override
    public int getType() {
        return 0;
    }

}
