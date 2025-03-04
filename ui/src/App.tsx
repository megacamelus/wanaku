import {
  IbmDynamicRouteServer,
  LogoGithub,
  Notification,
  Search,
} from "@carbon/icons-react";
import {
  Column,
  Content,
  Grid,
  Header,
  HeaderContainer,
  HeaderGlobalAction,
  HeaderGlobalBar,
  HeaderMenuButton,
  HeaderMenuItem,
  HeaderName,
  HeaderNavigation,
  SkipToContent,
} from "@carbon/react";
import { Link, Outlet } from "react-router-dom";
import "./App.scss";
import { ExternalLinks, Links } from "./router/links.models";
import axios from "axios";

function App() {
  axios.defaults.baseURL = import.meta.env.VITE_WANAKU_ROUTER_BASE_URL || 'http://localhost:8080';

  const action = (click: string) => () => {
    console.log(click);
  };

  return (
    <HeaderContainer
      render={({ isSideNavExpanded, onClickSideNavExpand }) => (
        <>
          <Header aria-label="Platform Name">
            <SkipToContent />
            <HeaderMenuButton
              aria-label={isSideNavExpanded ? "Close menu" : "Open menu"}
              onClick={onClickSideNavExpand}
              isActive={isSideNavExpanded}
              aria-expanded={isSideNavExpanded}
            />
            <HeaderName href={ExternalLinks.Home} target="_blank" prefix="">
              <IbmDynamicRouteServer size={32} style={{ marginRight: "1em" }} />{" "}
              Wanaku
            </HeaderName>

            <HeaderNavigation aria-label="Wanaku">
              <HeaderMenuItem as={Link} to={Links.Home}>
                Home
              </HeaderMenuItem>
              <HeaderMenuItem as={Link} to={Links.Tools}>
                Tools
              </HeaderMenuItem>
              <HeaderMenuItem as={Link} to={Links.Resources}>
                Resources
              </HeaderMenuItem>
            </HeaderNavigation>
            <HeaderGlobalBar>
              <HeaderGlobalAction
                aria-label="Search"
                onClick={action("search click")}
              >
                <Search size={20} />
              </HeaderGlobalAction>
              <HeaderGlobalAction
                aria-label="Notifications"
                onClick={action("notification click")}
              >
                <Notification size={20} />
              </HeaderGlobalAction>
              <HeaderGlobalAction
                aria-label="GitHub"
                onClick={() => {
                  window.open(ExternalLinks.GitHub, "_blank");
                }}
                tooltipAlignment="end"
              >
                <LogoGithub size={20} />
              </HeaderGlobalAction>
            </HeaderGlobalBar>
          </Header>

          <Content id="main-content">
            <Grid fullWidth>
              <Column lg={16} md={8} sm={4}>
                <Outlet />
              </Column>
            </Grid>
          </Content>
        </>
      )}
    />
  );
}

export default App;
