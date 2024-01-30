import UserSidebar from "@/components/sidebar/UserSidebar";
import { MainLayout } from "@/layouts/MainLayout";
import TeamWorkspaceBody from "./components/TeamWorkspaceBody";

export default function TeamWorkspacePage() {
  return (
    <MainLayout variant="horizontal">
      <UserSidebar />
      <div className="flex items-center justify-center w-full h-full">
      <TeamWorkspaceBody />
      </div>
    </MainLayout>
  );
}