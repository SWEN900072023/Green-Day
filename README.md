# Music Events System - Green Day

It is the project repository for the team Green Day in SWEN90007 Software Design and Architecture. During the project, the team implements an online music events system (a.k.a. MES) consisting of a React application and a Java web server and deploys it to Render.

## Team Overview

The team comprises four Master of Software Engineering students from the University of Melbourne.

- Quanchi Chen
- Yijie (Steven) Xie
- Wenxuan Xie
- Jingning (Benjamin) Qian

## Deployment

### React Application

```bash
# URL
https://react-qydn.onrender.com/
```

### Web Service

```bash
# URL
https://mes-24tf.onrender.com/mes/

# Environment Variables
JAVA_OPTS : 
-Djdbc.url=jdbc:postgresql://dpg-cjfj1k8cfp5c739pg6s0-a/mes_0o3g -Djdbc.user=greenday -Djdbc.password=AFNlFQPBeKGd5sVusIYBdNh5cl9BIuQ4 -Dcors.origins.ui=https://react-qydn.onrender.com
```

### PostgreSQL Database

```bash
hostname : dpg-cjfj1k8cfp5c739pg6s0-a.singapore-postgres.render.com
port : 5432
database_name : mes_0o3g
username : greenday
password : AFNlFQPBeKGd5sVusIYBdNh5cl9BIuQ4
```

### Admin Account

```
username/email: root@test.io
password: 123456
```

## Repository Structure

| Directory             | Content                                                      |
| --------------------- | ------------------------------------------------------------ |
| docs/                 | All the process and product documents generated during the project |
| docs/part1/           | Assignment Part 1 report                                     |
| docs/part2/           | Assignment Part 2 report                                     |
| docs/meeting-minutes/ | All the weekly team meeting minutes                          |
| client/               | Source code for the React application                        |
| server/               | Source code for the Java web server                          |

## Change Log

| Date           | Change Log                                                |
| ----------- | ------------------------------------------------------------ |
| 19 Sep 2023 | Finalise implementing all the use cases described in the Part 1 A report. |
| 19 Sep 2023 | Add Part 2 report. |
| 20 Aug 2023 | Deploy MES to Render.                   |
| 19 Aug 2023 | Integrate the React application with the Java web server.<br/>Develop the customer signup page. |
| 18 Aug 2023 | Develop the customer signup API endpoint. |
| 16 Aug 2023 | Set up the PostgreSQL database.                        |
| 15 Aug 2023 | Add external links about page structures.<br/>Add Week 4 team meeting minutes. |
| 11 Aug 2023 | Add Part 1A submission.<br/>Add Week 3 team meeting minutes. |
| 7 Aug 2023  | Add Week 2 team meeting minutes.                             |
| 4 Aug 2023  | Add an initial README.md.                                    |

## External Links

The following three links contain the recommended page structure for each type of end user.

- [Page Structure (Customer)](https://online.visual-paradigm.com/community/share/swen90007-page-structure-customer--1gifgo0tnz)
- [Page Structure (Event Planner)](https://online.visual-paradigm.com/community/share/swen90007-page-structure-event-planner--1gifi7o138)
- [Page Structure (Administrator)](https://online.visual-paradigm.com/community/share/swen90007-page-structure-administrator--1gifiplxhs)
