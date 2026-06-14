# Ping Player Name and SPR Display(PPSD)

[English](#english) | [中文](#中文)

---

## English

### 📖 Description

A Fabric client-side mod for Minecraft 1.21.8 that displays your **Ping**, **Server Packet Rate (SPR)**, **Bandwidth**, and **Player Name** in real time on the bottom‑left corner of the screen. It also provides a one‑key copy function for network information.

### ✨ Features

- **Real‑time HUD**  
  Shows Ping (ms), SPR (packets/sec), Bandwidth (uplink/downlink KiB/s) and your in‑game name.
- **Color‑coded thresholds**  
  Ping and SPR change color according to predefined ranges to indicate network quality.
- **Bandwidth monitoring**  
  Displays up‑to‑date upload/download speed based on actual Netty traffic.
- **History statistics**  
  Automatically records Ping, SPR and bandwidth for the last minute and since joining the server (average, min, max).
- **One‑key copy**  
  Press `N` to copy a compact summary, or `Ctrl + N` to copy detailed statistics – both include bandwidth data.
- **Lag warnings**  
  Warns in chat when Ping stays abnormally high or bandwidth drops significantly (thresholds based on historical averages).

### 📥 Installation

1. Install **Fabric Loader** (≥0.17.0) and **Fabric API** (≥0.130.0) for Minecraft 1.21.8.
2. Download the mod JAR file from the release page.
3. Put the JAR into your `.minecraft/mods` folder.
4. Launch the game and join a multiplayer server.

### 🎮 How to Use

- **Display** – Fixed at lower‑left corner, two lines:
  ```
  Bandwidth: Upl: 12.3 KiB/s | Dnl: 456.7 KiB/s
  ▃▅█ Ping: 23 ms  SPR: 18.5 pkt/s  ID: Steve
  ```
- **Copy network info**  
  - **Simple copy** (`N`):  
    `Ping: 23 ms  SPR: 18.5 pkt/s  Bandwidth: Upl: 12.3 KiB/s | Dnl: 456.7 KiB/s  Player Name: Steve  Time: 14:05:23`
  - **Detailed copy** (`Ctrl + N`):  
    Includes server address, player name, time, current values, 1‑minute stats, and lifetime stats for Ping, SPR, uplink and downlink bandwidth.
- **Lag warnings** (appear in chat):  
  - `[PPSD] Warning: Ping abnormally high` – when Ping stays > (lifetime avg + 500 ms) for 15 seconds.  
  - `[PPSD] Warning: bandwidth (Upl/Dnl) abnormally low` – when bandwidth drops below 50% of lifetime average for 15 seconds.

### ⚙️ Configuration

No config file yet. You can change the copy key binding in **Options → Controls → category “PPSD - Copy Information”**.

### 🛠️ Building from Source

```bash
git clone <repository>
cd ping-player-name-and-spr-display
./gradlew build
```

The built JAR will be in `build/libs/`.

### 📄 License

MIT

### 🙏 Acknowledgements

Thanks to Fabric, the Minecraft modding community, Deepseek.

---

## 中文

### 📖 简介

本模组为 Minecraft 1.21.8 Fabric 客户端模组，在屏幕左下角实时显示你的 **网络延迟(Ping)**、**服务器每秒发包数(SPR)**、**带宽(上行 Upl/下行 Dnl)** 以及 **玩家名**，并提供一键复制网络信息的功能。

### ✨ 功能特性

- **实时 HUD 显示**  
  显示 Ping(ms)、SPR(packets/sec)、带宽(KiB/s) 以及玩家名。
- **颜色阈值**  
  Ping 和 SPR 根据预设范围自动变色，直观反映网络状况。
- **带宽监控**  
  基于 Netty 实际流量统计上行/下行速度，单位 KiB/s。
- **历史统计**  
  自动记录最近1分钟及加入服务器以来的 Ping、SPR 和带宽平均值/最大/最小值。
- **一键复制**  
  按 `N` 键复制简洁版网络信息，按 `Ctrl + N` 复制详细统计（均包含带宽数据）。
- **卡顿警告**  
  当 Ping 持续偏高或带宽明显下降时，在聊天栏发出警告（阈值基于历史平均值）。

### 📥 安装方法

1. 为 Minecraft 1.21.8 安装 **Fabric Loader** (≥0.17.0) 和 **Fabric API** (≥0.130.0)。
2. 下载本模组的 JAR 文件。
3. 将 JAR 放入 `.minecraft/mods` 文件夹。
4. 启动游戏并加入多人服务器。

### 🎮 使用方法

- **显示位置** – 固定在屏幕左下角，共两行：
  ```
  Bandwidth: Upl: 12.3 KiB/s | Dnl: 456.7 KiB/s
  ▃▅█ Ping: 23 ms  SPR: 18.5 pkt/s  ID: Steve
  ```
- **复制网络信息**  
  - **简洁版** (按 `N`)：  
    `Ping: 23 ms  SPR: 18.5 pkt/s  Bandwidth: Upl: 12.3 KiB/s | Dnl: 456.7 KiB/s  Player Name: Steve  Time: 14:05:23`
  - **详细版** (按 `Ctrl + N`)：  
    包含服务器地址、玩家名、时间、当前值、最近1分钟统计以及全程统计（Ping、SPR、上下行带宽）。
- **卡顿警告**（出现在聊天栏）：  
  - `[PPSD]警告：Ping异常升高` – 当 Ping 持续15秒高于（全程平均+500ms）。  
  - `[PPSD]警告：带宽（Upl/Dnl）异常降低` – 当带宽持续15秒低于全程平均值的50%。

### ⚙️ 配置

暂无配置文件。你可以在 **选项 → 控制** 中找到 **“PPSD-复制”** 分类，修改复制热键。

### 🛠️ 从源码构建

```bash
git clone <仓库地址>
cd ping-player-name-and-spr-display
./gradlew build
```

生成的 JAR 位于 `build/libs/`。

### 📄 许可证

MIT

### 🙏 致谢

梁文峰牛逼

---
