<html><body>
<p style="font-weight: normal; line-height: 1.2; color: rgb(51, 51, 51); font-family: sans-serif, Arial, Verdana, 'Trebuchet MS';"><span style="font-size:20px;"><span style="font-family:verdana,geneva,sans-serif;">&nbsp;<strong>jTwitchPlayer</strong>&nbsp;</span></span></p>

<p style="color: rgb(51, 51, 51); font-family: sans-serif, Arial, Verdana, 'Trebuchet MS'; font-size: 13px; line-height: 20.7999992370605px;"><em><span style="font-family:verdana,geneva,sans-serif;"><strong><span style="font-size:12px;">Requirements:</span></strong></span></em></p>

<ul style="padding: 0px 40px; color: rgb(51, 51, 51); font-family: sans-serif, Arial, Verdana, 'Trebuchet MS'; font-size: 13px; line-height: 20.7999992370605px;">
	<li style="line-height: 20.7999992370605px;"><span style="font-family:verdana,geneva,sans-serif;"><span style="font-size: 12px;">Installed version of VLC (VideoLan Client) [<a href="http://www.videolan.org/vlc/download-windows.html">w</a><span style="color: rgb(102, 0, 

255);"><a href="http://www.videolan.org/vlc/download-windows.html">indows</a></span>]<span style="color: rgb(102, 0, 255);">&nbsp;</span>[<a href="http://www.videolan.org/vlc/download-debian.html">debian</a>]</span></span>

	<ul>
		<li style="line-height: 20.7999992370605px;"><span style="font-family:verdana,geneva,sans-serif;"><span style="font-size: 11px;">You need the x86/x64 version depending on your java version</span></span></li>
	</ul>
	</li>
	<li style="line-height: 20.7999992370605px;"><span style="font-family:verdana,geneva,sans-serif;"><span style="font-size: 12px;">Installed/Unzipped version of livestreamer [<a href="https://github.com/chrippa/livestreamer/releases">download</a>]</span></span>
	<ul>
		<li style="line-height: 20.7999992370605px;"><span style="font-family:verdana,geneva,sans-serif;"><span style="font-size: 11px;">If you have an unzipped version of livestreamer on your computer.&nbsp;<span style="line-height: 20.7999992370605px;">You need to specifiy the path to livestreamer in&nbsp;<span style="color: rgb(102, 0, 255);"><strong>livestreamer.properties </strong></span><strong><sup>*You have to run the jTwitchPlayer once to create property files</sup></strong></span></span></span></li>
		<li style="line-height: 20.7999992370605px;"><span style="font-family:verdana,geneva,sans-serif;"><span style="font-size: 11px;"><strong><span style="line-height: 20.7999992370605px;">Example:&nbsp;</span></strong></span></span>
		<ul>
			<li style="line-height: 20.7999992370605px;"><span style="font-family:verdana,geneva,sans-serif;"><span style="line-height: 20.7999992370605px;"><span style="font-size: 11px;">livestreamer=C:/users/myusername/livestreamer/</span>&nbsp;&nbsp;&nbsp;<span style="color: rgb(255, 0, 0);"><span style="font-size: 10px;">&nbsp;</span></span></span><strong><span style="color: rgb(255, 0, 0);"><small><span style="line-height: 20.7999992370605px;"><span style="font-size: 10px;">!Use slashes to replace windows specific backslashes</span></span></small></span></strong></span></li>
		</ul>
		</li>
	</ul>
	</li>
</ul>

<ul style="padding: 0px 40px; color: rgb(51, 51, 51); font-family: sans-serif, Arial, Verdana, 'Trebuchet MS'; font-size: 13px; line-height: 20.7999992370605px;">
	<li>
	<p><span style="font-family:verdana,geneva,sans-serif;"><span style="font-size:12px;">Copy the jtwitch.player.&lt;version&gt;.jar into an empty folder and run it once.</span><strong>&nbsp;<small><span style="color:#FF0000;"><span style="font-size:10px;">!This will create the property file</span></span></small></strong></span></p>
	</li>
</ul>

<p style="color: rgb(51, 51, 51); font-family: sans-serif, Arial, Verdana, 'Trebuchet MS'; font-size: 13px; line-height: 20.7999992370605px;"><br />
<span style="font-family:verdana,geneva,sans-serif;"><span style="font-size:12px;">You can specifiy the following variables in&nbsp;jtwitch.properties:</span></span></p>

<ul>
	<li style="color: rgb(51, 51, 51); font-family: sans-serif, Arial, Verdana, 'Trebuchet MS'; font-size: 13px; line-height: 20.7999992370605px;"><span style="font-size:11px;"><span style="font-family:verdana,geneva,sans-serif;">username=&lt;twitch-username&gt;&nbsp;&nbsp; &nbsp;// Your twitch username which follows your favorite streamer</span></span></li>
	<li style="color: rgb(51, 51, 51); font-family: sans-serif, Arial, Verdana, 'Trebuchet MS'; font-size: 13px; line-height: 20.7999992370605px;"><span style="font-size:11px;"><span style="font-family:verdana,geneva,sans-serif;">volume=&lt;default-volume&gt;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;// Default player volume at startup</span></span></li>
</ul>

<hr />
<p style="color: rgb(51, 51, 51); font-family: sans-serif, Arial, Verdana, 'Trebuchet MS'; font-size: 13px; line-height: 20.7999992370605px;"><em><span style="font-family:verdana,geneva,sans-serif;"><strong><span style="font-size:12px;">Running jTwitchPlayer (grab executable from releases):</span></strong></span></em></p>

<p style="color: rgb(51, 51, 51); font-family: sans-serif, Arial, Verdana, 'Trebuchet MS'; font-size: 13px; line-height: 20.7999992370605px; margin-left: 40px;"><span style="font-family:verdana,geneva,sans-serif;"><span style="font-size:11px;"><strong>All platforms:</strong></span></span></p>

<p style="color: rgb(51, 51, 51); font-family: sans-serif, Arial, Verdana, 'Trebuchet MS'; font-size: 13px; line-height: 20.7999992370605px; margin-left: 80px;"><span style="font-family:verdana,geneva,sans-serif;"><span style="font-size:11px;">java -jar jtwitch.player.&lt;version_number&gt;.jar</span></span></p>

<p style="color: rgb(51, 51, 51); font-size: 13px; line-height: 20.7999992370605px; margin-left: 40px;"><span style="font-family:verdana,geneva,sans-serif;"><strong><span style="font-size:11px;">Windows:</span></strong></span></p>

<p style="color: rgb(51, 51, 51); font-size: 13px; line-height: 20.7999992370605px; margin-left: 80px;"><span style="font-family:verdana,geneva,sans-serif;"><span style="font-size:11px;">Double click jar file.</span></span></p>

<hr />
<p><em><span style="font-family:verdana,geneva,sans-serif;"><span style="font-size:12px;"><strong>Special thanks to all library creators:</strong></span></span></em></p>

<ul>
	<li><span style="font-size:11px;"><span style="font-family:verdana,geneva,sans-serif;">gson</span></span></li>
	<li><span style="font-size:11px;"><span style="font-family:verdana,geneva,sans-serif;">jkeymaster</span></span></li>
	<li><span style="font-size:11px;"><span style="font-family:verdana,geneva,sans-serif;">jna</span></span></li>
	<li><span style="font-size:11px;"><span style="font-family:verdana,geneva,sans-serif;">log4j</span></span></li>
	<li><span style="font-size:11px;"><span style="font-family:verdana,geneva,sans-serif;">vlcj</span></span></li>
	<li><span style="font-size:11px;"><span style="font-family:verdana,geneva,sans-serif;">slfj4</span></span></li>
	<li><span style="font-size:11px;"><span style="font-family:verdana,geneva,sans-serif;">commons-lang</span></span></li>
	<li><span style="font-size:11px;"><span style="font-family:verdana,geneva,sans-serif;">logback</span></span></li>
</ul>

<hr />
<p><span style="font-family:verdana,geneva,sans-serif;"><small>If there are any issues or questions, please send me an email.&nbsp;<a href="mailto:info@g-design.org">info@g-design.org</a></small></span></p>
![jtwitchplayerscreen1](https://cloud.githubusercontent.com/assets/3350441/8478825/f79c559a-20d3-11e5-8e0e-c9998e959236.png)

</body>
</html>
