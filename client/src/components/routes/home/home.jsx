import { Fragment, useEffect, useState } from "react";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import Divider from "@mui/material/Divider";
import ListItemText from "@mui/material/ListItemText";
import ListItemAvatar from "@mui/material/ListItemAvatar";
import Avatar from "@mui/material/Avatar";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";
import "./home.css";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DateCalendar } from "@mui/x-date-pickers/DateCalendar";
const Home = () => {
  const exampleEvents = [
    { label: "Brunch this weekend?", year: 1994 },
    { label: "Summer BBQ", year: 1972 },
    { label: "Oui Oui", year: 1974 },
  ];
  const [text, setText] = useState("");
  const [optionText, setOptionText] = useState("");

  const searchEvent = exampleEvents.filter((event) => {
    return event.label.toLowerCase().includes(text.toLowerCase());
  });

  return (
    <div className="container">
      <div className="eventsList">
        <List
          sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}
        >
          {searchEvent.map((event) => {
            return (
              <>
                <ListItem alignItems="flex-start">
                  <ListItemAvatar>
                    <Avatar
                      alt="Remy Sharp"
                      src="/static/images/avatar/1.jpg"
                    />
                  </ListItemAvatar>
                  <ListItemText
                    primary={`${event.label}`}
                    secondary={
                      <Fragment>
                        <Typography
                          sx={{ display: "inline" }}
                          component="span"
                          variant="body2"
                          color="text.primary"
                        >
                          Ali Connors
                        </Typography>
                        {" — I'll be in your neighborhood doing errands this…"}
                      </Fragment>
                    }
                  />
                </ListItem>
                <Divider variant="inset" component="li" />
              </>
            );
          })}
        </List>
      </div>
      <div className="searchBar-calendar">
        <Autocomplete
          inputValue={text}
          onInputChange={(e, newValue) => {
            console.log(newValue);
            setText(newValue);
          }}
          disablePortal
          id="controllable-states-demo"
          options={exampleEvents}
          sx={{ width: 500 }}
          renderInput={(params) => (
            <TextField
              {...params}
              label="Type Music Events"
              //   onChange={(e) => {
              //     console.log(e.target.value);
              //     setText(e.target.value);
              //   }
              // }
            />
          )}
        />
        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <DateCalendar sx={{ marginTop: "5rem" }} />
        </LocalizationProvider>
      </div>
    </div>
  );
};

export default Home;
