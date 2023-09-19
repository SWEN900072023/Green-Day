import { Fragment, useEffect, useState } from "react";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import Divider from "@mui/material/Divider";
import ListItemText from "@mui/material/ListItemText";
import ListItemAvatar from "@mui/material/ListItemAvatar";
import Avatar from "@mui/material/Avatar";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Form from "../../form-input/form-input";
import Button from "../../button/button";
import { useNavigate } from "react-router-dom";
import AxiosApi from "../../axiosAPI/api";
import "./../home/home.css";
import { useSelector } from "react-redux";
import { selectorCurrentUser } from "../../store/user/user.selector";

const User = () => {
  const navigate = useNavigate();
  const currentUser = useSelector(selectorCurrentUser);
  const [users, setUsers] = useState([]);
  const exampleEvents = [
    { label: "Brunch this weekend?", year: 1994 },
    { label: "Summer BBQ", year: 1972 },
    { label: "Oui Oui", year: 1974 },
  ];
  useEffect(() => {
    async function getUsersInfo() {
      await AxiosApi.get(`/admin/users`, {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }).then((res) => {
        console.log(res);
        setUsers(res.data.data);
      });
    }
    getUsersInfo();
  }, []);
  const createPlanner = () => {};
  return (
    <>
      <h1>hi, user info page</h1>
      <div className="homeContainer">
        {" "}
        <div className="eventsList">
          {/* TODO: For event planner, it should display the event that they hosted */}
          <h1>All user info</h1>
          <List
            sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}
          >
            {users.length === 0 ? (
              <></>
            ) : (
              users.map((user) => {
                return (
                  <>
                    <ListItem
                      className="listItem"
                      alignItems="flex-start"
                      // onClick={() => {
                      //   console.log(`hi im ${event.label}`);
                      //   // TODO: Soft coded event name
                      //   navigate("/Summer BBQ/booking");
                      // }}
                    >
                      <ListItemAvatar></ListItemAvatar>
                      <ListItemText
                        primary={`${user.firstName} ${user.lastName}`}
                        secondary={
                          <Fragment>
                            <Typography
                              sx={{ display: "inline" }}
                              component="span"
                              variant="body2"
                              color="text.primary"
                            >
                              Email: {user.email}
                            </Typography>
                          </Fragment>
                        }
                      />
                    </ListItem>
                    {/* <button>cancel booking</button> */}
                    <Divider variant="inset" component="li" />
                  </>
                );
              })
            )}
          </List>
        </div>
        <div className="searchBar-calendar">
          <div className="event-creation-container">
            <div className="creation-container">
              <form onSubmit={createPlanner}>
                <Form
                  label="Email"
                  type="email"
                  required
                  name="Email"
                  value=""
                  //   onChange={(e) => setTitle(e.target.value)}
                ></Form>
                <Form
                  label="Password"
                  type="password"
                  required
                  //   onChange={(e) => setArtist(e.target.value)}
                  name="Password"
                  value=""
                ></Form>
                <Form
                  label="First Name"
                  type="text"
                  required
                  //   onChange={(e) => setStartTime(e.target.value)}
                  name="First Name"
                  value=""
                ></Form>
                <Form
                  label="Last Name"
                  type="text"
                  required
                  //   onChange={(e) => setEndTime(e.target.value)}
                  name="Last Name"
                  value=""
                ></Form>
              </form>
            </div>
            {/* <div className="section-creation-container"></div> */}
          </div>
          <div className="button-group">
            <Button type="submit" buttonType="google">
              Create Planner
            </Button>
            {/* <Button>Cancel event</Button> */}
          </div>
        </div>
      </div>
    </>
  );
};
export default User;
