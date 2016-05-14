# Dcard Crawler

這是一個用來抓取 [Dcard][1] 上公開資料的 Java 程式。

## 功能

- 可以用來下載 Dcard 指定版面、指定頁面的文章
- 可以指定文章的作者性別
- 可以下載文章中所包含的圖片 (不含留言中的)

## 環境需求

- Java Runtime Environment 1.7+
- 文字輸入介面 (Command Line Terminal)

## 使用方式

此程式的執行方法如下，在沒有任何選項的情況下，會下載所有文章之中前五頁的文章：

```
java -jar dcard-crawler.jar [選項] [下載路徑]
```

下載路徑即為檔案儲存的資料夾位置，可以是相對也可以是絕對路徑。

目前可以使用的選項有這些：

```
-e,--ex-no-img-posts        不要下載不含圖片的文章
-f,--forum <forum alias>    指定討論版 (必須使用別名)
-g,--gender <M|F>           指定作者性別 (M=男生, F=女生)
-i,--download-images        下載文章中的圖片
-p,--page-num <start:end>   指定頁碼 (起始:結束)
-v,--version                顯示程式版本號碼
```

可以使用的討論版別名如下：

```
ALL, BOOK, ACG, GAME, CCC, FUNNY, BG, TREADING, TALK, GIRL, BOY,
MOOD, MUSIC, TRAVEL, PHOTOGRAPHY, MOVIE, HOROSCOPES, LITERATURE, SPORT,
PET, FOOD, HANDICRAFTS, JOB, STUDYABROAD, MARVEL, FRESHMAN, COURSE,
EXAM, SEX, DCARD, WHYSOSERIOUS;
```

## 執行範例

假設我今天想下載攝影版 (`-f PHOTOGRAPHY`) 前三頁文章 (`-p 1:3`) 的圖片 (`-i`) 到我的資料夾 `photos`，而且不想要把沒有圖片的文章抓下來 (`-e`)，可以這樣執行：

```
java -jar dcard-crawler.jar -e -f PHOTOGRAPHY -i -p 1:3 photos
```

執行期間大概會看到這些資訊：

```
五月 14, 2016 6:41:25 下午 slmt.crawler.dcard.downloader.DcardPostDownloader <init>
資訊: creates a new directory at photos
五月 14, 2016 6:41:26 下午 slmt.crawler.dcard.downloader.DcardPostDownloader downloadPosts
資訊: retrieving page no.1
五月 14, 2016 6:41:48 下午 slmt.crawler.dcard.downloader.DcardPostDownloader downloadPosts
資訊: page no.1 is downloaded
五月 14, 2016 6:41:48 下午 slmt.crawler.dcard.downloader.DcardPostDownloader downloadPosts
資訊: retrieving page no.2
五月 14, 2016 6:42:09 下午 slmt.crawler.dcard.downloader.DcardPostDownloader downloadPosts
資訊: page no.2 is downloaded
五月 14, 2016 6:42:09 下午 slmt.crawler.dcard.downloader.DcardPostDownloader downloadPosts
資訊: retrieving page no.3
五月 14, 2016 6:42:31 下午 slmt.crawler.dcard.downloader.DcardPostDownloader downloadPosts
資訊: page no.3 is downloaded
五月 14, 2016 6:42:31 下午 slmt.crawler.dcard.downloader.ImageDownloader downloadImages
資訊: downloading images in 210419345.txt
五月 14, 2016 6:42:34 下午 slmt.crawler.dcard.downloader.ImageDownloader downloadImages
資訊: finishes downloading for 210419345.txt

(省略)
```

程式會分成兩階段執行。首先下載文章，再去分析文章的內容來下載圖片。因此前面的訊息是在提示目前正在下載第幾頁的文章，後面則在說目前正下載哪篇文章的圖片。檔案名稱是他的 Dcard 文章編號。

下載過程視使用者設定，以及當前 Dcard 的流量可能會花費數分鐘甚至更長的時間。

下載結束之後，`photos` 資料夾內應該有這些檔案 (沒有全部列出)：

![IMAGE ALT TEXT HERE](example.png)

文字檔像是 `210419345.txt` 即為文章，數字為 Dcard 文章編號。圖片則是文章內的附圖，檔名則為 Dcard 文章編號加上一個序號。

## 部分程式碼尚未整理

這份程式碼原本是為了我個人研究用途所撰寫的，因此部分程式碼尚未整理。目前只開放部分功能供大家使用。

## 程式碼相依性

此程式編譯需要以下 library，這些都已經寫入 Maven 的 `pom.xml` 之中，理論上可以直接用 Maven 編譯。

- [Apache Commons CLI][2] - 1.3.1
  - 用來解析 command line options
- [Apache Commons Logging][3] - 1.2.0
  - 用來記錄一些資訊
- [Alibaba Fastjson][4] - 1.2.4
  - 用來解析 JSON

公布的 Jar 檔則已經包含這些程式庫。

## TODOs

- 將之前留下的程式碼轉換為可以使用的功能
  - CSV 下載器 (主要是為了能夠存進 database)
  - 簽名檔計數器
- 下載下來的文章可以用 HTML 的格式存，這樣打開可以直接看到圖片
- 將文章下載與圖片下載這兩個功能分開 (這樣已經有文章就不用重新下載一遍)
- 聲音檔下載器
- GUI (應該不太可能做，不過還是寫一下)

[1]: https://www.dcard.tw/
[2]: https://commons.apache.org/proper/commons-cli/
[3]: https://commons.apache.org/proper/commons-logging/
[4]: https://github.com/alibaba/fastjson
