<style lang="scss">
  /* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
  @import "@/uni_modules/uview-ui/index.scss";
</style>

<script>
  import { getInfo } from 'api/system.js';

  export default {
    onLaunch: function() {
      console.log('App Launch')
    },
    onShow: function() {
      getInfo().then();
    },
    onHide: function() {
      console.log('App Hide')
    }
  }
</script>
