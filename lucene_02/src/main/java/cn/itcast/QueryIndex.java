package cn.itcast;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

/**
 * Created by on 2018/8/3.
 */
public class QueryIndex {

    /**
     * 需求：根据关键词查询索引库
     * 查询索引库原理：根据关键词匹配最小分词单元，进行搜索
     * 思考：关键词是否大于最小分词单元，如果大于最小分词单元，那么关键词必须要分词。
     */
    @Test
    public void searchIndex() throws Exception {

        //指定搜索关键词
        String qname = "传智播客";
        //创建查询解析器，对查询关键词进行分词
        QueryParser parser = new QueryParser(Version.LUCENE_4_10_3, "title", new IKAnalyzer());
        //对查询关键词进行分词
        Query query = parser.parse(qname);

        //抽取查询索引库代码块
        this.executeAndPrintresult(query);


    }


    /**
     * 需求：根据关键词查询索引库,同时查询多个索引域字段
     * 查询索引库原理：根据关键词匹配最小分词单元，进行搜索
     * 思考：关键词是否大于最小分词单元，如果大于最小分词单元，那么关键词必须要分词。
     */
    @Test
    public void multiIndex() throws Exception {

        //指定搜索关键词
        String qname = "非常优秀";
        //创建多字段匹配查询解析器，对查询关键词进行分词
        MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_4_10_3,
                new String[]{"title", "desc", "content"},
                new IKAnalyzer());
        //对查询关键词进行分词
        Query query = parser.parse(qname);

        //抽取查询索引库代码块
        this.executeAndPrintresult(query);


    }


    /**
     * 需求：使用termQuery查询索引库
     */
    @Test
    public void termQueryIndex() throws Exception {

        //指定搜索关键词
        String qname = "传智播客";
        //创建termQuery
        //此时qname查询关键词没有分词，直接传递给TermQuery进行匹配索引库查询
        TermQuery query = new TermQuery(new Term("title",qname));

        //抽取查询索引库代码块
        this.executeAndPrintresult(query);


    }

    /**
     * 需求：使用termQuery查询索引库
     */
    @Test
    public void FuzzyQueryIndex() throws Exception {

        //指定搜索关键词
        String qname = "XuSene";
        //创建termQuery
        //此时qname查询关键词没有分词，直接传递给TermQuery进行匹配索引库查询
        FuzzyQuery query = new FuzzyQuery(new Term("title",qname));

        //抽取查询索引库代码块
        this.executeAndPrintresult(query);


    }


    /**
     * 需求：使用numericRangeQuery进行范围搜索
     */
    @Test
    public void numericRangeQueryIndex() throws Exception {

        //创建numericRangeQuery
        //参数1：指定查询域字段
        //参数2：指定查询起始位置
        //参数3：指定查询结束位置
        //参数4：左边开【不包含】(闭【包含】)区间
        //参数5：右边开【不包含】(闭【包含】)区间
        NumericRangeQuery query = NumericRangeQuery.newLongRange("id", 5L, 15L, false, true);
        //抽取查询索引库代码块
        this.executeAndPrintresult(query);


    }


    /**
     * 需求：使用booleanQuery进行组合查询
     */
    @Test
    public void booleanQueryIndex() throws Exception {

        //创建组合查询对象
        BooleanQuery query = new BooleanQuery();

        //创建查询所有对象
        MatchAllDocsQuery query1 = new MatchAllDocsQuery();

        //创建numericRangeQuery
        //参数1：指定查询域字段
        //参数2：指定查询起始位置
        //参数3：指定查询结束位置
        //参数4：左边开【不包含】(闭【包含】)区间
        //参数5：右边开【不包含】(闭【包含】)区间
        NumericRangeQuery query2 = NumericRangeQuery.newLongRange("id", 5L, 15L, false, true);

        //组合query1,query2查询
        query.add(query1, BooleanClause.Occur.MUST);
        //求补集
        query.add(query2, BooleanClause.Occur.MUST_NOT);


        //抽取查询索引库代码块
        this.executeAndPrintresult(query);


    }

    /**
     * 抽取查询索引库代码块
     *
     * @param query
     * @throws Exception
     */
    private void executeAndPrintresult(Query query) throws Exception {

        //指定存储索引库路径
        String path = "C:\\lessons\\indexs";
        //读取索引库索引
        DirectoryReader r = DirectoryReader.open(FSDirectory.open(new File(path)));
        //创建查询索引库核心对象
        IndexSearcher indexSearcher = new IndexSearcher(r);

        //使用搜索核心对象进行搜索
        //文档概要信息：
        //文档得分
        //文档id
        //文档命中总记录数
        TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
        //获取文档命中总记录数
        int totalHits = topDocs.totalHits;
        System.out.println("命中总记录数：" + totalHits);

        //获取文档得分数组对象
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        //循环文档得分，文档id的数组
        for (ScoreDoc doc : scoreDocs) {

            //获取文档得分
            float score = doc.score;
            System.out.println("文档得分：" + score);
            //文档id
            int docID = doc.doc;
            System.out.println("文档ID:" + docID);

            //根据文档id获取文档对象
            Document document = indexSearcher.doc(docID);
            //获取域字段id
            String id = document.get("id");
            System.out.println("文档域id:" + id);
            //获取标题
            String title = document.get("title");
            System.out.println("文档标题:" + title);

            //描述
            String desc = document.get("desc");
            System.out.println("文档描述:" + desc);

            //内容
            String content = document.get("content");
            System.out.println("文档内容:" + content);


        }


    }

}
