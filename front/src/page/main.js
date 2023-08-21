import React from "react";
import Sidebar from "../component/Sidebar";
import Aside from "../component/aside";
import { styled } from "styled-components";
import TopQuestionList from "../question/toppuestion";

const MainContainer = styled.div`
  display: flex;
  flex-direction: row;
  margin: 0 120px;
`;

const SidebarContainer = styled.div`
  width: 15%;
`;

const QuestionListContainer = styled.div`
  width: 55%;
`;

const AsideContainer = styled.div`
  width: 30%;
`;

function Main() {
  return (
    <>
      <MainContainer>
        <SidebarContainer>
          <Sidebar />
        </SidebarContainer>
        <QuestionListContainer>
          <TopQuestionList />
        </QuestionListContainer>
        <AsideContainer>
          <Aside />
        </AsideContainer>
      </MainContainer>
    </>
  );
}

export default Main;
