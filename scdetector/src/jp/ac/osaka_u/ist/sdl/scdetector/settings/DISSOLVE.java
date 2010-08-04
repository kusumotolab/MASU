package jp.ac.osaka_u.ist.sdl.scdetector.settings;

public enum DISSOLVE {

	TRUE {

		@Override
		public boolean isDissolve() {
			return true;
		}
	},

	FALSE {

		@Override
		public boolean isDissolve() {
			return false;
		}
	};

	public abstract boolean isDissolve();
}
