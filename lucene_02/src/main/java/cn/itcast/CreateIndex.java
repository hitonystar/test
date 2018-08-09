package cn.itcast;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

/**
 * Created by on 2018/8/3.
 */
public class CreateIndex {

    /**
     * 需求：创建索引库索引
     */
    @Test
    public void addIndex() throws Exception {
        //指定索引库索引磁盘存储路径
        String path = "C:\\lessons\\indexs";
        //创建目录对象，关联索引库磁盘存储位置
        FSDirectory directory = FSDirectory.open(new File(path));

        //4,创建第三方中文分词器ik
        Analyzer analyzer = new IKAnalyzer();


        //创建写索引库核心对象配置对象： lucene版本，分词器
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);


        //创建写索引库核心对象
        IndexWriter indexWriter = new IndexWriter(directory, iwc);

        for (int i = 0; i < 30; i++) {

            //创建文档对象
            Document doc = new Document();
            //添加id字段： 文档编号
            //StringField: 域字段类型，类似数据库varChar
            //特点：不分词，有索引，Store.NO/YES (原则：搜索结果如果不在页面进行展示，不存储)
            doc.add(new StringField("id", "u01" + i, Field.Store.NO));
            //标题
            //TextField: 域字段类型
            //特点：分词，索引，Store.YES
            TextField field = new TextField("title", "黄晓明在传智播客学习java,lucene经典教程", Field.Store.YES);
            if (i == 22) {
                field.setBoost(10000);
            }
            doc.add(field);
            //描述
            doc.add(new TextField("desc", " Lucene是apache软件基金会4 jakarta项目组的一个子项目," +
                    "是一个开放源代码的全文检索引擎工具包," +
                    "但它不是一个完整的全文检索引擎,而是一个全文检索引擎", Field.Store.YES));
            //内容
            //TextField： 分词，索引，Store.NO
            doc.add(new TextField("content", "Lucene是一个非常优秀的开源的全文搜索引擎;" +
                    " 我们可以在它的上面开发出各种全文搜索的应用来。" +
                    "Lucene在国外有很高的知名度; 现在已经是Apache的顶级项目; 在国内", Field.Store.NO));


            //写索引库
            indexWriter.addDocument(doc);

        }


        //提交
        indexWriter.commit();


    }


    /**
     * 需求：更新索引库索引
     */
    @Test
    public void updateIndex() throws Exception {
        //指定索引库索引磁盘存储路径
        String path = "C:\\lessons\\indexs";
        //创建目录对象，关联索引库磁盘存储位置
        FSDirectory directory = FSDirectory.open(new File(path));

        //4,创建第三方中文分词器ik
        Analyzer analyzer = new IKAnalyzer();


        //创建写索引库核心对象配置对象： lucene版本，分词器
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);


        //创建写索引库核心对象
        IndexWriter indexWriter = new IndexWriter(directory, iwc);


        //创建文档对象
        Document doc = new Document();
        //添加id字段： 文档编号
        //StringField: 域字段类型，类似数据库varChar
        //特点：不分词，有索引，Store.NO/YES (原则：搜索结果如果不在页面进行展示，不存储)
        doc.add(new StringField("id", "u0100000000" , Field.Store.NO));
        //标题
        //TextField: 域字段类型
        //特点：分词，索引，Store.YES
        TextField field = new TextField("title", "黄晓明在传智播客学习java,lucene经典教程,凤姐在传智播客学习ui", Field.Store.YES);

        doc.add(field);
        //描述
        doc.add(new TextField("desc", " Lucene是apache软件基金会4 jakarta项目组的一个子项目," +
                "是一个开放源代码的全文检索引擎工具包," +
                "但它不是一个完整的全文检索引擎,而是一个全文检索引擎", Field.Store.YES));
        //内容
        //TextField： 分词，索引，Store.NO
        doc.add(new TextField("content", "Lucene是一个非常优秀的开源的全文搜索引擎;" +
                " 我们可以在它的上面开发出各种全文搜索的应用来。" +
                "Lucene在国外有很高的知名度; 现在已经是Apache的顶级项目; 在国内", Field.Store.NO));


        //写索引库
        //更新原理：先查询，再删除，再添加
        //将会查询出id=u011的文档对象，然后把此文档对象删除，删除完毕后把doc文档对象添加到索引库即可
        indexWriter.updateDocument(new Term("title","传智播客"),doc);

        //提交
        indexWriter.commit();


    }

    /**
     * 需求：删除索引库
     */
    @Test
    public void deleteIndex() throws Exception {
        //指定索引库索引磁盘存储路径
        String path = "C:\\lessons\\indexs";
        //创建目录对象，关联索引库磁盘存储位置
        FSDirectory directory = FSDirectory.open(new File(path));

        //4,创建第三方中文分词器ik
        Analyzer analyzer = new IKAnalyzer();


        //创建写索引库核心对象配置对象： lucene版本，分词器
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);


        //创建写索引库核心对象
        IndexWriter indexWriter = new IndexWriter(directory, iwc);


        //先查询--再删除
        //根据title关键词查询，把查询的文档全部删除
        indexWriter.deleteDocuments(new Term("title","传智播客"));

        //提交
        indexWriter.commit();


    }



}
